package com.example.demo.service.impl;

import com.example.demo.constants.GitHubConstants;
import com.example.demo.dto.GitHubContributorDto;
import com.example.demo.dto.GitHubRepoDto;
import com.example.demo.dto.GitHubUserDto;
import com.example.demo.service.GitHubApiService;
import com.example.demo.util.AppLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
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
            AppLogger.LOGGER.info("Fetching repositories from: " + url);
            
                ResponseEntity<List<GitHubRepoDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<GitHubRepoDto>>() {}
                );
                List<GitHubRepoDto> repos = response.getBody();

                AppLogger.LOGGER.info("Retrieved " + (repos != null ? repos.size() : 0) + " repositories");
                return repos != null ? repos : List.of();
        } catch (Exception e) {
            AppLogger.LOGGER.severe("Error fetching repositories: " + e.getMessage());
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
            AppLogger.LOGGER.info("Fetching contributors first page from: " + url);

                ResponseEntity<List<GitHubContributorDto>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<GitHubContributorDto>>() {}
                );
                List<GitHubContributorDto> list = response.getBody();
                return list != null ? list : List.of();
        } catch (Exception e) {
            AppLogger.LOGGER.severe("Error fetching contributors for " + repoName + ": " + e.getMessage());
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
            AppLogger.LOGGER.info("Fetching user details from: " + url);
            
            GitHubUserDto user = restTemplate.getForObject(url, GitHubUserDto.class);

            AppLogger.LOGGER.info("Retrieved user details for: " + username);
            return user;
        } catch (Exception e) {
            AppLogger.LOGGER.severe("Error fetching user details for " + username + ": " + e.getMessage());
            return new GitHubUserDto();
        }
    }
}
