package com.example.demo.dto;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitHubRepoDto {
    private Long id;
    private String name;

    @SerializedName("stargazers_count")
    private Integer stargazersCount;

    @SerializedName("watchers_count")
    private Integer watchersCount;

    private String language;

    @SerializedName("open_issues_count")
    private Integer openIssuesCount;

    private GithubLicenseDTO license;

    private String url;

    private String description;
}