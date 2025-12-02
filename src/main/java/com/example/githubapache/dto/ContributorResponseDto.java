package com.example.githubapache.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContributorResponseDto {
    private Long id;
    private String repositoryName;
    private String username;
    private String location;
    private String company;
    private Integer contributions;
    private String profileUrl;
    private String avatarUrl;
}
