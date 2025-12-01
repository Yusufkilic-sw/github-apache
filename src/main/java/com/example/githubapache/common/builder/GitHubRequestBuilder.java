package com.example.githubapache.common.builder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.example.githubapache.common.constants.GitHubConstants;

import lombok.Getter;

/**
 * Builder pattern implementation for constructing GitHub API request URLs
 * Provides fluent API for building complex API queries
 */
@Getter
public class GitHubRequestBuilder {    
    private String organization;
    private String repository;
    private String username;
    private Integer perPage;
    private Integer page;
    private String sortBy;
    private String direction;
    private String filterLanguage;
    private final Map<String, String> customParams;
    private RequestType requestType;
    
    /**
     * Request type enumeration for different GitHub API endpoints
     */
    public enum RequestType {
        ORG_REPOS,          // /orgs/{org}/repos
        REPO_CONTRIBUTORS,  // /repos/{org}/{repo}/contributors
        USER_DETAILS        // /users/{username}
    }
    
    private GitHubRequestBuilder() {
        this.customParams = new HashMap<>();
        this.perPage = 30;
        this.page = 1;
        this.direction = "desc";
    }
    
    public static GitHubRequestBuilder builder() {
        return new GitHubRequestBuilder();
    }
    
    public GitHubRequestBuilder organization(String organization) {
        this.organization = organization;
        return this;
    }
    
    public GitHubRequestBuilder repository(String repository) {
        this.repository = repository;
        return this;
    }
    
    public GitHubRequestBuilder username(String username) {
        this.username = username;
        return this;
    }
    
    public GitHubRequestBuilder perPage(Integer perPage) {
        this.perPage = perPage;
        return this;
    }
    
    public GitHubRequestBuilder page(Integer page) {
        this.page = page;
        return this;
    }
    
    public GitHubRequestBuilder sortBy(String sortBy) {
        this.sortBy = sortBy;
        return this;
    }
    
    public GitHubRequestBuilder direction(String direction) {
        this.direction = direction;
        return this;
    }
    
    public GitHubRequestBuilder filterLanguage(String language) {
        this.filterLanguage = language;
        return this;
    }
    
    public GitHubRequestBuilder addCustomParam(String key, String value) {
        this.customParams.put(key, value);
        return this;
    }
    
    public GitHubRequestBuilder forOrgRepositories() {
        this.requestType = RequestType.ORG_REPOS;
        return this;
    }
    
    public GitHubRequestBuilder forRepositoryContributors() {
        this.requestType = RequestType.REPO_CONTRIBUTORS;
        return this;
    }
    
    public GitHubRequestBuilder forUserDetails() {
        this.requestType = RequestType.USER_DETAILS;
        return this;
    }
    
    /**
     * Build the complete URL string
     */
    public String build() {
        if (requestType == null) {
            throw new IllegalStateException("Request type must be specified (forOrgRepositories, forRepositoryContributors, forUserDetails)");
        }
        
        return switch (requestType) {
            case ORG_REPOS -> buildOrgRepositoriesUrl();
            case REPO_CONTRIBUTORS -> buildRepositoryContributorsUrl();
            case USER_DETAILS -> buildUserDetailsUrl();
        };
    }
    
    private String buildOrgRepositoriesUrl() {
        if (organization == null || organization.isBlank()) {
            throw new IllegalStateException("Organization must be specified for ORG_REPOS request");
        }
        
        StringBuilder url = new StringBuilder();
        url.append( GitHubConstants.BASE_URL).append("/orgs/").append(organization).append("/repos");
        
        appendQueryParams(url);
        return url.toString();
    }
    
    private String buildRepositoryContributorsUrl() {
        if (organization == null || organization.isBlank()) {
            throw new IllegalStateException("Organization must be specified for REPO_CONTRIBUTORS request");
        }
        if (repository == null || repository.isBlank()) {
            throw new IllegalStateException("Repository must be specified for REPO_CONTRIBUTORS request");
        }
        
        StringBuilder url = new StringBuilder();
        String encodedRepo = URLEncoder.encode(repository, StandardCharsets.UTF_8);
        url.append( GitHubConstants.BASE_URL).append("/repos/").append(organization).append("/").append(encodedRepo).append("/contributors");
        
        appendQueryParams(url);
        return url.toString();
    }
    
    private String buildUserDetailsUrl() {
        if (username == null || username.isBlank()) {
            throw new IllegalStateException("Username must be specified for USER_DETAILS request");
        }
        
        StringBuilder url = new StringBuilder();
        String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8);
        url.append( GitHubConstants.BASE_URL).append("/users/").append(encodedUsername);
        
        // User details endpoint doesn't use query params typically
        return url.toString();
    }
    
    private void appendQueryParams(StringBuilder url) {
        boolean hasParams = false;
        
        // Always add pagination params for org repos and contributors
        if (requestType == RequestType.ORG_REPOS || requestType == RequestType.REPO_CONTRIBUTORS) {
            url.append("?per_page=").append(perPage);
            url.append("&page=").append(page);
            hasParams = true;
            
            // Add sorting for org repos
            if (requestType == RequestType.ORG_REPOS && sortBy != null && !sortBy.isBlank()) {
                url.append("&sort=").append(sortBy);
                url.append("&direction=").append(direction);
            }
        }
        
        // Add language filter if specified
        if (filterLanguage != null && !filterLanguage.isBlank()) {
            url.append(hasParams ? "&" : "?");
            url.append("language=").append(URLEncoder.encode(filterLanguage, StandardCharsets.UTF_8));
            hasParams = true;
        }
        
        // Add any custom parameters
        for (Map.Entry<String, String> entry : customParams.entrySet()) {
            url.append(hasParams ? "&" : "?");
            url.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            hasParams = true;
        }
    }
    
    /**
     * Get the base URL for debugging purposes
     */
    public String getBaseUrl() {
        return  GitHubConstants.BASE_URL;
    }
    
}
