package com.example.githubapache.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitHubContributorResponse {
    private String login;
    
    @JsonProperty("avatar_url")
    private String avatarUrl;
    
    @JsonProperty("html_url")
    private String profileUrl;
    
    private String url;
    
    private Integer contributions;
}
