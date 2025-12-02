package com.example.githubapache.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitHubUserResponse {
    private String login;
    private String location;
    private String company;
    private String name;
    
    @JsonProperty("html_url")
    private String profileUrl;
}
