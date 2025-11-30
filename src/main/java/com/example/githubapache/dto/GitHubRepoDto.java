package com.example.githubapache.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitHubRepoDto {
    private Long id;
    private String name;

    private Integer stargazersCount;

    private Integer watchersCount;

    private String language;

    private Integer openIssuesCount;

    private GithubLicenseDTO license;

    private String url;

    private String description;
}