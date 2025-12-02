package com.example.githubapache.service;

import java.util.List;

import com.example.githubapache.dto.GitHubContributorDto;
import com.example.githubapache.dto.GitHubRepoDto;
import com.example.githubapache.dto.GitHubUserDto;

public interface GitHubApiService {
    
    List<GitHubRepoDto> getLatestRepositories(int perPage);
    
    List<GitHubContributorDto> getContributors(String repoName);
    
    GitHubUserDto getUserDetails(String username);
}
