package com.example.githubapache.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryResponseDto {
    private Long id;
    private String name;
    private Integer stargazerCount;
    private Integer watchersCount;
    private String language;
    private Integer openIssuesCount;
    private String licenseKey;
    private String licenseName;
    private String url;
    private String description;
}
