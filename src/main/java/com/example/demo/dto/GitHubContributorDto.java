package com.example.demo.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitHubContributorDto {
    private String login;

    @SerializedName("avatar_url")
    private String avatarUrl;

    @SerializedName("profile_url")
    private String profileUrl;

    private String url;

    private Integer contributions;
}