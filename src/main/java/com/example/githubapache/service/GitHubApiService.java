package com.example.githubapache.service;

import java.util.List;

import com.example.githubapache.dto.GitHubContributorDto;
import com.example.githubapache.dto.GitHubRepoDto;
import com.example.githubapache.dto.GitHubUserDto;

/**
 * Service interface for GitHub API operations
 */
public interface GitHubApiService {
    
    /**
     * Get latest repositories from GitHub organization
     */
    List<GitHubRepoDto> getLatestRepositories(int perPage);
    
    /**
     * Get contributors for a specific repository
     */
    List<GitHubContributorDto> getContributors(String repoName);
    
    /**
     * Get user details from GitHub
     */
    GitHubUserDto getUserDetails(String username);
}
