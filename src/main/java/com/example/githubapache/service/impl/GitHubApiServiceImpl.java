package com.example.githubapache.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.common.constants.GitHubConstants;
import com.example.githubapache.dto.GitHubContributorDto;
import com.example.githubapache.dto.GitHubRepoDto;
import com.example.githubapache.dto.GitHubUserDto;
import com.example.githubapache.service.GitHubApiService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
            String url = String.format(
                "%s/orgs/%s/repos?per_page=%d&sort=updated&direction=desc",
                GitHubConstants.BASE_URL, GitHubConstants.ORG, perPage
            );
            log.info("Fetching repositories from: {}", url);
            
                ResponseEntity<List<GitHubRepoDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
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
            String encodedRepo = URLEncoder.encode(repoName, StandardCharsets.UTF_8);
            String url = String.format(
                "%s/repos/%s/%s/contributors?per_page=%d&page=%d",
                GitHubConstants.BASE_URL, GitHubConstants.ORG, encodedRepo, perPage, page
            );
            log.info("Fetching contributors first page from: {}", url);

                ResponseEntity<List<GitHubContributorDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
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
            String encodedUser = URLEncoder.encode(username, StandardCharsets.UTF_8);
            String url = String.format("%s/users/%s", GitHubConstants.BASE_URL, encodedUser);
            log.info("Fetching user details from: {}", url);
            
            GitHubUserDto user = restTemplate.getForObject(url, GitHubUserDto.class);

            log.info("Retrieved user details for: {}", username);
            return user;
        } catch (Exception e) {
            log.error("Error fetching user details for {}: ", username, e);
            return new GitHubUserDto();
        }
    }
}
