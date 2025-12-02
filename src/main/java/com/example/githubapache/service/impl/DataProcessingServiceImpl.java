package com.example.githubapache.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.githubapache.dto.GitHubContributorDto;
import com.example.githubapache.dto.GitHubRepoDto;
import com.example.githubapache.dto.GitHubUserDto;
import com.example.githubapache.entity.ContributorEntity;
import com.example.githubapache.entity.RepositoryEntity;
import com.example.githubapache.mapper.ContributorMapper;
import com.example.githubapache.mapper.RepositoryMapper;
import com.example.githubapache.service.ContributorService;
import com.example.githubapache.service.DataProcessingService;
import com.example.githubapache.service.GitHubApiService;
import com.example.githubapache.service.RepositoryService;
import com.example.githubapache.metrics.ProcessingMetrics;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataProcessingServiceImpl implements DataProcessingService {

    private final GitHubApiService githubApiService;
    private final RepositoryService repositoryService;
    private final ContributorService contributorService;
    private final RepositoryMapper repositoryMapper;
    private final ContributorMapper contributorMapper;
    private final ProcessingMetrics processingMetrics;

    @Transactional
    public void processApacheRepositories() {
        log.info("Starting Apache repositories processing...");
        var timer = processingMetrics.startProcessingTimer();
        try {
            List<GitHubRepoDto> allRepos = fetchAllRepositories();
            if (allRepos.isEmpty()) {
                log.warn("No repositories found");
                return;
            }

            List<GitHubRepoDto> topRepos = selectTopRepositories(allRepos, 5);

            for (GitHubRepoDto repoDto : topRepos) {
                processRepository(repoDto);
            }

            log.info("Apache repositories processing completed successfully");
        } catch (Exception e) {
            log.error("Error processing Apache repositories", e);
            processingMetrics.incrementErrors();
            throw e;
        } finally {
            processingMetrics.recordProcessingTime(timer);
        }
    }

    private List<GitHubRepoDto> fetchAllRepositories() {
        return githubApiService.getLatestRepositories(100);
    }

    private List<GitHubRepoDto> selectTopRepositories(List<GitHubRepoDto> allRepos, int limit) {
        return allRepos.stream()
                .sorted(Comparator.comparing(GitHubRepoDto::getStargazersCount, Comparator.nullsLast(Integer::compareTo)).reversed())
                .limit(limit)
                .toList();
    }

    private void processRepository(GitHubRepoDto repoDto) {
        try {
            RepositoryEntity repo = repositoryMapper.toEntity(repoDto);
            repo.setId(repoDto.getId());
            repositoryService.saveRepository(repo);
            processingMetrics.incrementRepositoriesProcessed();
            
            List<GitHubContributorDto> contributorsAll = fetchContributorsForRepository(repo.getName());
            List<GitHubContributorDto> top10 = selectTopContributors(contributorsAll, 10);
            for (GitHubContributorDto contribDto : top10) {
                processContributor(contribDto, repo.getName());
            }
        } catch (Exception e) {
            log.error("Error processing repository: {}", repoDto.getName(), e);
            processingMetrics.incrementErrors();
        }
    }

    private List<GitHubContributorDto> fetchContributorsForRepository(String repoName) {
        return githubApiService.getContributors(repoName);
    }

    private List<GitHubContributorDto> selectTopContributors(List<GitHubContributorDto> allContribs, int limit) {
        return allContribs.stream()
                .sorted(Comparator.comparing(GitHubContributorDto::getContributions, Comparator.nullsLast(Integer::compareTo)).reversed())
                .limit(limit)
                .toList();
    }

    private void processContributor(GitHubContributorDto contribDto, String repositoryName) {
        GitHubUserDto userDto = githubApiService.getUserDetails(contribDto.getLogin());
        try {
            ContributorEntity contributor = contributorMapper.toEntity(contribDto, userDto, repositoryName);
            contributorService.saveContributor(contributor);
            processingMetrics.incrementContributorsProcessed();
        } catch (Exception e) {
            log.error("Error processing contributor {}: ", contribDto.getLogin(), e);
            processingMetrics.incrementErrors();
        }
    }
}