package com.example.githubapache.util;

import com.example.githubapache.response.GitHubContributorResponse;
import com.example.githubapache.response.GitHubLicenseResponse;
import com.example.githubapache.response.GitHubRepoResponse;
import com.example.githubapache.response.GitHubUserResponse;

public final class GitHubResponseTestUtil{
    private GitHubResponseTestUtil() {}
    
    // Default factory methods
    public static GitHubRepoResponse getRepoResponse() {
        return repoResponse(1L, "test-repo", 100);
    }

    public static GitHubContributorResponse getContributorResponse() {
        return contributorResponse("test-user", 42);
    }

    public static GitHubUserResponse getUserResponse() {
        return userResponse("test-user", "Istanbul", "Apache", "Test User");
    }
    
    public static GitHubRepoResponse repoResponse(Long id, String name, int stars) {
        GitHubRepoResponse r = new GitHubRepoResponse();
        r.setId(id);
        r.setName(name);
        r.setStargazersCount(stars);
        return r;
    }

    public static GitHubLicenseResponse licenseResponse(String key, String name) {
        GitHubLicenseResponse l = new GitHubLicenseResponse();
        l.setKey(key);
        l.setName(name);
        return l;
    }

    public static GitHubLicenseResponse getLicenseResponse() {
        return licenseResponse("apache-2.0", "Apache License 2.0");
    }

    public static GitHubContributorResponse contributorResponse(String login, int contributions) {
        GitHubContributorResponse c = new GitHubContributorResponse();
        c.setLogin(login);
        c.setContributions(contributions);
        return c;
    }

    public static GitHubUserResponse userResponse(String login, String location, String company, String name) {
        GitHubUserResponse u = new GitHubUserResponse();
        u.setLogin(login);
        u.setLocation(location);
        u.setCompany(company);
        u.setName(name);
        return u;
    }
}
