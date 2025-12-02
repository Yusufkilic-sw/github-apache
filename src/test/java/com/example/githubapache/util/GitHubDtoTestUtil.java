package com.example.githubapache.util;

import com.example.githubapache.dto.GitHubContributorDto;
import com.example.githubapache.dto.GitHubRepoDto;
import com.example.githubapache.dto.GitHubUserDto;

public final class GitHubDtoTestUtil {
    private GitHubDtoTestUtil() {}

    // Default factory methods
    public static GitHubRepoDto getRepoDto() {
        return repoDto(1L, "test-repo", 100);
    }

    public static GitHubContributorDto getContributorDto() {
        return contributorDto("test-user", 42);
    }

    public static GitHubUserDto getUserDto() {
        return userDto("test-user", "Istanbul", "Apache", "Test User");
    }

    public static GitHubRepoDto repoDto(Long id, String name, int stars) {
        return new GitHubRepoDto(id, name, stars, null, null, null, null, null, null);
    }

    public static GitHubContributorDto contributorDto(String login, int contributions) {
        return new GitHubContributorDto(login, null, null, null, contributions);
    }

    public static GitHubUserDto userDto(String login, String location, String company, String name) {
        return new GitHubUserDto(login, location, company, name, null);
    }
}
