package com.example.githubapache.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitHubUserDto {
    private String login;
    private String location;
    private String company;
    private String name;
    private String profileUrl;
    
}
