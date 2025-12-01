package com.example.githubapache.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.githubapache.common.constants.GitHubConstants;
import com.example.githubapache.common.builder.GitHubRequestBuilder;
import com.example.githubapache.dto.GitHubContributorDto;
import com.example.githubapache.dto.GitHubRepoDto;
import com.example.githubapache.dto.GitHubUserDto;
import com.example.githubapache.service.GitHubApiService;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class GitHubApiServiceImpl implements GitHubApiService {

    private final RestTemplate restTemplate;
    
    /**
     * Belirtilen organizasyonun son güncellenen 100 repository'sini getirir
     */
    public List<GitHubRepoDto> getLatestRepositories(int perPage) {
        try {
            String url = GitHubRequestBuilder.builder()
                    .forOrgRepositories()
                    .organization(GitHubConstants.ORG)
                    .perPage(perPage)
                    .sortBy("updated")
                    .direction("desc")
                    .build();
            
            log.info("Fetching repositories from: {}", url);
            Objects.requireNonNull(url, "GitHub repos URL must not be null");

            ResponseEntity<List<GitHubRepoDto>> response = restTemplate.exchange(
                url,
                Objects.requireNonNull(HttpMethod.GET, "HTTP method must not be null"),
                null,
                new ParameterizedTypeReference<List<GitHubRepoDto>>() {}
            );
            List<GitHubRepoDto> repos = response.getBody();

            log.info("Retrieved {} repositories", (repos != null ? repos.size() : 0));
            return repos != null ? repos : List.of();
        } catch (Exception e) {
            log.error("Error fetching repositories: ", e);
            return List.of();
        }
    }
    

    /**
     * Get all contributors for a repository by paging through the API (per_page=100).
     */
    public List<GitHubContributorDto> getContributors(String repoName) {
        final int perPage = 100;
        final int page = 1;
        try {
            String url = GitHubRequestBuilder.builder()
                    .forRepositoryContributors()
                    .organization(GitHubConstants.ORG)
                    .repository(repoName)
                    .perPage(perPage)
                    .page(page)
                    .build();
            
            log.info("Fetching contributors for repository: {} from: {}", repoName, url);
            Objects.requireNonNull(url, "GitHub contributors URL must not be null");

            ResponseEntity<List<GitHubContributorDto>> response = restTemplate.exchange(
                url,
                Objects.requireNonNull(HttpMethod.GET, "HTTP method must not be null"),
                null,
                new ParameterizedTypeReference<List<GitHubContributorDto>>() {}
            );
            List<GitHubContributorDto> list = response.getBody();
            return list != null ? list : List.of();
        } catch (Exception e) {
            log.error("Error fetching contributors for {}: ", repoName, e);
            return List.of();
        }
    }
    
    /**
     * Belirtilen kullanıcının detaylı bilgilerini getirir
     */
    public GitHubUserDto getUserDetails(String username) {
        try {
            String url = GitHubRequestBuilder.builder()
                    .forUserDetails()
                    .username(username)
                    .build();
            
            log.info("Fetching user details for: {}", username);
            Objects.requireNonNull(url, "GitHub user details URL must not be null");

            GitHubUserDto user = restTemplate.getForObject(url, GitHubUserDto.class);

            log.info("Retrieved user details for: {}", username);
            return user;
        } catch (Exception e) {
            log.error("Error fetching user details for {}: ", username, e);
            return new GitHubUserDto();
        }
    }
}
