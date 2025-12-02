package com.example.githubapache.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitHubRepoResponse {
    private Long id;
    private String name;
    
    @JsonProperty("stargazers_count")
    private Integer stargazersCount;
    
    @JsonProperty("watchers_count")
    private Integer watchersCount;
    
    private String language;
    
    @JsonProperty("open_issues_count")
    private Integer openIssuesCount;
    
    private GitHubLicenseResponse license;
    
    private String url;
    
    private String description;
}
