package com.example.githubapache.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitHubContributorDto {
    private String login;

    private String avatarUrl;

    private String profileUrl;

    private String url;

    private Integer contributions;
}