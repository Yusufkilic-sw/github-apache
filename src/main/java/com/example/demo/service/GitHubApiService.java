package com.example.demo.service;

import com.example.demo.dto.GitHubContributorDto;
import com.example.demo.dto.GitHubRepoDto;
import com.example.demo.dto.GitHubUserDto;

import java.util.List;

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
