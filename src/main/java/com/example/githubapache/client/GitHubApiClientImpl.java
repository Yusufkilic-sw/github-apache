package com.example.githubapache.client;

import java.util.List;
import java.util.Objects;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.example.githubapache.common.builder.GitHubRequestBuilder;
import com.example.githubapache.response.GitHubContributorResponse;
import com.example.githubapache.response.GitHubRepoResponse;
import com.example.githubapache.response.GitHubUserResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class GitHubApiClientImpl implements GitHubApiClient {

    private final RestTemplate restTemplate;

    @Override
    public List<GitHubRepoResponse> getOrgRepositories(String organization, int perPage, String sort, String direction) {
        String url = GitHubRequestBuilder.builder()
                .forOrgRepositories()
                .organization(organization)
                .perPage(perPage)
                .sortBy(sort)
                .direction(direction)
                .build();
                Objects.requireNonNull(url, "GitHub repositories URL must not be null");
        try {
                ResponseEntity<List<GitHubRepoResponse>> response = restTemplate.exchange(
                                url,
                                Objects.requireNonNull(HttpMethod.GET, "HttpMethod must not be null"),
                                null,
                                new ParameterizedTypeReference<List<GitHubRepoResponse>>() {});
                return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
                log.debug("Repos not found for org {} (404): {}", organization, e.getMessage());
                return List.of();
        } catch (HttpClientErrorException e) {
                log.warn("Client error fetching repos for org {}: status={} message={}", organization, e.getStatusCode(), e.getMessage());
                return List.of();
        } catch (HttpServerErrorException e) {
                log.warn("Server error fetching repos for org {}: status={} message={}", organization, e.getStatusCode(), e.getMessage());
                return List.of();
        }
    }

    @Override
    public List<GitHubContributorResponse> getRepositoryContributors(String organization, String repository, int perPage, int page) {
        String url = GitHubRequestBuilder.builder()
                .forRepositoryContributors()
                .organization(organization)
                .repository(repository)
                .perPage(perPage)
                .page(page)
                .build();
        Objects.requireNonNull(url, "GitHub contributors URL must not be null");
        try {
                ResponseEntity<List<GitHubContributorResponse>> response = restTemplate.exchange(
                                url,
                                Objects.requireNonNull(HttpMethod.GET, "HttpMethod must not be null"),
                                null,
                                new ParameterizedTypeReference<List<GitHubContributorResponse>>() {});
                return response.getBody();
        } catch (HttpClientErrorException.NotFound e) {
                log.debug("Contributors not found for {}/{} (404): {}", organization, repository, e.getMessage());
                return List.of();
        } catch (HttpClientErrorException e) {
                log.warn("Client error fetching contributors for {}/{}: status={} message={}", organization, repository, e.getStatusCode(), e.getMessage());
                return List.of();
        } catch (HttpServerErrorException e) {
                log.warn("Server error fetching contributors for {}/{}: status={} message={}", organization, repository, e.getStatusCode(), e.getMessage());
                return List.of();
        }
    }

    @Override
    public GitHubUserResponse getUserDetails(String username) {
        String url = GitHubRequestBuilder.builder()
                .forUserDetails()
                .username(username)
                .build();
        Objects.requireNonNull(url, "GitHub user details URL must not be null");
        try {
                return restTemplate.getForObject(url, GitHubUserResponse.class);
        } catch (HttpClientErrorException.NotFound e) {
                log.debug("User not found {} (404)", username);
                return null;
        } catch (HttpClientErrorException e) {
                log.warn("Client error fetching user {}: status={} message={}", username, e.getStatusCode(), e.getMessage());
                return null;
        } catch (HttpServerErrorException e) {
                log.warn("Server error fetching user {}: status={} message={}", username, e.getStatusCode(), e.getMessage());
                return null;
        }
    }
}
