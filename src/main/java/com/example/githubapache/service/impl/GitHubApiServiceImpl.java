package com.example.githubapache.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.example.githubapache.common.constants.GitHubConstants;
import com.example.githubapache.client.GitHubApiClient;
import com.example.githubapache.dto.GitHubContributorDto;
import com.example.githubapache.dto.GitHubRepoDto;
import com.example.githubapache.dto.GitHubUserDto;
import com.example.githubapache.service.GitHubApiService;
import com.example.githubapache.response.GitHubRepoResponse;
import com.example.githubapache.response.GitHubContributorResponse;
import com.example.githubapache.response.GitHubUserResponse;
import com.example.githubapache.mapper.GitHubResponseMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GitHubApiServiceImpl implements GitHubApiService {

    private final GitHubApiClient gitHubApiClient;
    private final GitHubResponseMapper responseMapper;
    
    public List<GitHubRepoDto> getLatestRepositories(int perPage) {
        try {
            log.debug("Fetching repositories for org {} perPage {}", GitHubConstants.ORG, perPage);
            List<GitHubRepoResponse> responseList = gitHubApiClient.getOrgRepositories(GitHubConstants.ORG, perPage, "updated", "desc");
            List<GitHubRepoDto> repos = responseList != null ? responseMapper.toRepoDtoList(responseList) : List.of();
            log.info("Repositories fetched: {}", repos.size());
            return repos;
        } catch (Exception e) {
            log.error("Error fetching repositories: ", e);
            return List.of();
        }
    }
    
    public List<GitHubContributorDto> getContributors(String repoName) {
        final int perPage = 100;
        final int page = 1;
        try {
            log.debug("Fetching contributors for repository: {}", repoName);
            List<GitHubContributorResponse> responseList = gitHubApiClient.getRepositoryContributors(GitHubConstants.ORG, repoName, perPage, page);
            List<GitHubContributorDto> list = responseList != null ? responseList.stream()
                .map(r -> new GitHubContributorDto(r.getLogin(), r.getAvatarUrl(), r.getProfileUrl(), r.getUrl(), r.getContributions()))
                .toList() : List.of();
            return list;
        } catch (Exception e) {
            log.error("Error fetching contributors for {}: ", repoName, e);
            return List.of();
        }
    }
    
    public GitHubUserDto getUserDetails(String username) {
        try {
            log.debug("Fetching user details for: {}", username);
            GitHubUserResponse response = gitHubApiClient.getUserDetails(username);
            GitHubUserDto user = response != null ?
                    new GitHubUserDto(response.getLogin(), response.getLocation(), response.getCompany(), response.getName(), response.getProfileUrl()) :
                    new GitHubUserDto();
            log.debug("User details retrieved for: {}", username);
            return user;
        } catch (Exception e) {
            log.error("Error fetching user details for {}: ", username, e);
            return new GitHubUserDto();
        }
    }
}
