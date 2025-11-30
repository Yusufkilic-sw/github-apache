package com.example.demo.service.impl;

import com.example.demo.dto.GitHubContributorDto;
import com.example.demo.dto.GitHubRepoDto;
import com.example.demo.dto.GitHubUserDto;
import com.example.demo.entity.ContributorEntity;
import com.example.demo.entity.RepositoryEntity;
import com.example.demo.mapper.ContributorMapper;
import com.example.demo.mapper.RepositoryMapper;
import com.example.demo.service.ContributorService;
import com.example.demo.service.DataProcessingService;
import com.example.demo.service.GitHubApiService;
import com.example.demo.service.RepositoryService;
import com.example.demo.util.AppLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataProcessingServiceImpl implements DataProcessingService {

    private final GitHubApiService githubApiService;
    private final RepositoryService repositoryService;
    private final ContributorService contributorService;
    private final RepositoryMapper repositoryMapper;
    private final ContributorMapper contributorMapper;

    @Transactional
    public void processApacheRepositories() {
        AppLogger.LOGGER.info("Starting Apache repositories processing...");

        List<GitHubRepoDto> allRepos = githubApiService.getLatestRepositories(100);
        if (allRepos.isEmpty()) {
            AppLogger.LOGGER.warning("No repositories found");
            return;
        }

        List<GitHubRepoDto> topRepos = allRepos.stream()
                .sorted(Comparator.comparing(GitHubRepoDto::getStargazersCount, Comparator.nullsLast(Integer::compareTo)).reversed())
                .limit(5)
                .toList();

        for (GitHubRepoDto repoDto : topRepos) {

            RepositoryEntity repo = repositoryMapper.toEntity(repoDto);
            repo.setId(repoDto.getId());
            repositoryService.saveRepository(repo);

            // Fetch all contributors from GitHub and then pick top 10 locally
            List<GitHubContributorDto> contributorsAll = githubApiService.getContributors(repo.getName());
            List<GitHubContributorDto> top10 = contributorsAll.stream()
                    .sorted(Comparator.comparing(GitHubContributorDto::getContributions, Comparator.nullsLast(Integer::compareTo)).reversed())
                    .limit(10)
                    .toList();

            for (GitHubContributorDto contribDto : top10) {
                
                GitHubUserDto userDto = githubApiService.getUserDetails(contribDto.getLogin());        
                ContributorEntity contributor = contributorMapper.toEntity(contribDto, userDto, repo.getName());
                contributorService.saveContributor(contributor);
            }
        }

        AppLogger.LOGGER.info("Apache repositories processing completed successfully");
    }
}