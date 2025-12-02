package com.example.githubapache.client;

import java.util.List;

import com.example.githubapache.response.GitHubContributorResponse;
import com.example.githubapache.response.GitHubRepoResponse;
import com.example.githubapache.response.GitHubUserResponse;

public interface GitHubApiClient {

    List<GitHubRepoResponse> getOrgRepositories(String organization, int perPage, String sort, String direction);

    List<GitHubContributorResponse> getRepositoryContributors(String organization, String repository, int perPage, int page);

    GitHubUserResponse getUserDetails(String username);
}
