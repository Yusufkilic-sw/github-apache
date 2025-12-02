package com.example.githubapache.service.impl;

import com.example.githubapache.dto.GitHubContributorDto;
import com.example.githubapache.dto.GitHubRepoDto;
import com.example.githubapache.dto.GitHubUserDto;
import com.example.githubapache.entity.ContributorEntity;
import com.example.githubapache.entity.RepositoryEntity;
import com.example.githubapache.mapper.ContributorMapper;
import com.example.githubapache.mapper.RepositoryMapper;
import com.example.githubapache.metrics.ProcessingMetrics;
import com.example.githubapache.service.ContributorService;
import com.example.githubapache.service.GitHubApiService;
import com.example.githubapache.service.RepositoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import static com.example.githubapache.util.GitHubDtoTestUtil.*;
import static com.example.githubapache.util.EntityTestUtil.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DataProcessingServiceImplTest {

    @InjectMocks
    private DataProcessingServiceImpl dataProcessingService;

    @Mock
    private GitHubApiService githubApiService;

    @Mock
    private RepositoryService repositoryService;

    @Mock
    private ContributorService contributorService;

    @Mock
    private RepositoryMapper repositoryMapper;

    @Mock
    private ContributorMapper contributorMapper;

    @Mock
    private ProcessingMetrics processingMetrics;

   

    @BeforeEach
    void setUp() {
        // nothing special
    }

    @Test
    void processApacheRepositories_savesReposAndContributors() {
        // Arrange
        GitHubRepoDto repoDto = getRepoDto();
        when(githubApiService.getLatestRepositories(100)).thenReturn(List.of(repoDto));

        RepositoryEntity repoEntity = getRepositoryEntity();
        when(repositoryMapper.toEntity(repoDto)).thenReturn(repoEntity);

        GitHubContributorDto contribDto = getContributorDto();
        when(githubApiService.getContributors("test-repo")).thenReturn(List.of(contribDto));

        GitHubUserDto userDto = getUserDto();
        when(githubApiService.getUserDetails(contribDto.getLogin())).thenReturn(userDto);

        ContributorEntity contributorEntity = contributorEntity("test-repo", contribDto.getLogin());
        when(contributorMapper.toEntity(contribDto, userDto, "test-repo")).thenReturn(contributorEntity);

        // Act
        dataProcessingService.processApacheRepositories();

        // Assert
        verify(repositoryService, times(1)).saveRepository(repoEntity);
        verify(contributorService, times(1)).saveContributor(contributorEntity);
        verify(processingMetrics, atLeastOnce()).incrementRepositoriesProcessed();
        verify(processingMetrics, atLeastOnce()).incrementContributorsProcessed();
    }

    @Test
    void processApacheRepositories_noRepositories_doesNothing() {
        when(githubApiService.getLatestRepositories(100)).thenReturn(List.of());

        dataProcessingService.processApacheRepositories();

        verify(repositoryService, never()).saveRepository(any());
        verify(contributorService, never()).saveContributor(any());
        verify(processingMetrics, never()).incrementRepositoriesProcessed();
        verify(processingMetrics, never()).incrementContributorsProcessed();
        verify(processingMetrics, atLeastOnce()).recordProcessingTime(any());
    }

    @Test
    void processApacheRepositories_topLevelException_incrementsErrorAndRethrows() {
        when(githubApiService.getLatestRepositories(100)).thenThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, () -> dataProcessingService.processApacheRepositories());

        verify(processingMetrics, atLeastOnce()).incrementErrors();
        verify(processingMetrics, atLeastOnce()).recordProcessingTime(any());
        verifyNoInteractions(repositoryService, contributorService);
    }

    @Test
    void processApacheRepositories_selectsTopFiveRepositoriesByStars() {
        GitHubRepoDto r1 = repoDto(1L, "r1", 10);
        GitHubRepoDto r2 = repoDto(2L, "r2", 60);
        GitHubRepoDto r3 = repoDto(3L, "r3", 30);
        GitHubRepoDto r4 = repoDto(4L, "r4", 80);
        GitHubRepoDto r5 = repoDto(5L, "r5", 20);
        GitHubRepoDto r6 = repoDto(6L, "r6", 70);
        when(githubApiService.getLatestRepositories(100)).thenReturn(List.of(r1, r2, r3, r4, r5, r6));

        // Map any repoDto to a corresponding entity keeping the name/id
        when(repositoryMapper.toEntity(any(GitHubRepoDto.class))).thenAnswer(inv -> {
            GitHubRepoDto dto = inv.getArgument(0);
            RepositoryEntity e = new RepositoryEntity();
            e.setId(dto.getId());
            e.setName(dto.getName());
            return e;
        });

        // No contributors for simplicity
        when(githubApiService.getContributors(anyString())).thenReturn(List.of());

        dataProcessingService.processApacheRepositories();

        ArgumentCaptor<RepositoryEntity> captor = ArgumentCaptor.forClass(RepositoryEntity.class);
        verify(repositoryService, times(5)).saveRepository(captor.capture());
        List<RepositoryEntity> saved = captor.getAllValues();
        // Expected order by stars desc: r4(80), r6(70), r2(60), r3(30), r5(20)
        assertEquals(List.of("r4", "r6", "r2", "r3", "r5"), saved.stream().map(RepositoryEntity::getName).toList());
    }

    @Test
    void processApacheRepositories_repositoryProcessingError_skipsAndContinues() {
        GitHubRepoDto bad = repoDto(1L, "bad", 100);
        GitHubRepoDto good = repoDto(2L, "good", 90);
        when(githubApiService.getLatestRepositories(100)).thenReturn(List.of(bad, good));

        when(repositoryMapper.toEntity(bad)).thenThrow(new RuntimeException("map fail"));
        RepositoryEntity goodEntity = new RepositoryEntity();
        goodEntity.setId(good.getId());
        goodEntity.setName(good.getName());
        when(repositoryMapper.toEntity(good)).thenReturn(goodEntity);
        when(githubApiService.getContributors("good")).thenReturn(List.of());

        dataProcessingService.processApacheRepositories();

        verify(processingMetrics, atLeastOnce()).incrementErrors();
        verify(repositoryService, times(1)).saveRepository(goodEntity);
        verify(contributorService, never()).saveContributor(any());
    }

    @Test
    void processApacheRepositories_contributorProcessingError_incrementsErrors() {
        GitHubRepoDto repoDto = repoDto(1L, "test-repo", 100);
        when(githubApiService.getLatestRepositories(100)).thenReturn(List.of(repoDto));
        RepositoryEntity repoEntity = new RepositoryEntity();
        repoEntity.setId(1L);
        repoEntity.setName("test-repo");
        when(repositoryMapper.toEntity(repoDto)).thenReturn(repoEntity);

        GitHubContributorDto c = contributorDto("user1", 10);
        when(githubApiService.getContributors("test-repo")).thenReturn(List.of(c));
        GitHubUserDto user = userDto("user1", "City", "Co", "User");
        when(githubApiService.getUserDetails("user1")).thenReturn(user);

        when(contributorMapper.toEntity(c, user, "test-repo")).thenThrow(new RuntimeException("persist fail"));

        dataProcessingService.processApacheRepositories();

        verify(processingMetrics, atLeastOnce()).incrementErrors();
        verify(contributorService, never()).saveContributor(any());
    }

    @Test
    void processApacheRepositories_selectTopContributorsByCount_descending() {
        GitHubRepoDto repoDto = repoDto(1L, "test-repo", 100);
        when(githubApiService.getLatestRepositories(100)).thenReturn(List.of(repoDto));
        RepositoryEntity repoEntity = new RepositoryEntity();
        repoEntity.setId(1L);
        repoEntity.setName("test-repo");
        when(repositoryMapper.toEntity(repoDto)).thenReturn(repoEntity);

        GitHubContributorDto c1 = contributorDto("a", 1);
        GitHubContributorDto c2 = contributorDto("b", 100);
        GitHubContributorDto c3 = contributorDto("c", 50);
        when(githubApiService.getContributors("test-repo")).thenReturn(List.of(c1, c2, c3));

        when(githubApiService.getUserDetails(anyString())).thenAnswer(inv -> {
            String login = inv.getArgument(0);
            return userDto(login, "City", "Co", login.toUpperCase());
        });

        when(contributorMapper.toEntity(any(GitHubContributorDto.class), any(GitHubUserDto.class), eq("test-repo")))
                .thenAnswer(inv -> {
                    GitHubContributorDto dto = inv.getArgument(0);
                    ContributorEntity ce = new ContributorEntity();
                    ce.setRepositoryName("test-repo");
                    ce.setUsername(dto.getLogin());
                    return ce;
                });

        dataProcessingService.processApacheRepositories();

        InOrder inOrder = inOrder(githubApiService);
        // Should request user details in order of contributions desc: b(100), c(50), a(1)
        inOrder.verify(githubApiService).getUserDetails("b");
        inOrder.verify(githubApiService).getUserDetails("c");
        inOrder.verify(githubApiService).getUserDetails("a");

        verify(contributorService, times(3)).saveContributor(any());
        verify(processingMetrics, atLeastOnce()).incrementRepositoriesProcessed();
        verify(processingMetrics, atLeast(3)).incrementContributorsProcessed();
    }
}
