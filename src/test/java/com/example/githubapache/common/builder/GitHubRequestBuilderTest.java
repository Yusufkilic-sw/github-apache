package com.example.githubapache.common.builder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GitHubRequestBuilderTest {

    @Test
    void buildOrgReposUrl_containsExpectedParams() {
        String url = GitHubRequestBuilder.builder()
                .forOrgRepositories()
                .organization("apache")
                .perPage(100)
                .sortBy("updated")
                .direction("desc")
                .filterLanguage("Java")
                .addCustomParam("type", "public")
                .build();

        assertTrue(url.startsWith("https://api.github.com/orgs/apache/repos"));
        assertTrue(url.contains("per_page=100"));
        assertTrue(url.contains("sort=updated"));
        assertTrue(url.contains("direction=desc"));
        assertTrue(url.contains("language=Java") || url.contains("language=Java"));
        assertTrue(url.contains("type=public"));
    }

    @Test
    void buildContributorsUrl_encodesRepoName() {
        String repoName = "my repo with spaces";
        String url = GitHubRequestBuilder.builder()
                .forRepositoryContributors()
                .organization("apache")
                .repository(repoName)
                .perPage(50)
                .page(2)
                .build();

        assertTrue(url.startsWith("https://api.github.com/repos/apache/"));
        assertTrue(url.contains("contributors"));
        assertTrue(url.contains("per_page=50"));
        assertTrue(url.contains("page=2"));
        // encoded space should be present
        assertTrue(url.contains("my+repo") || url.contains("my%20repo"));
    }

    @Test
    void buildUserDetailsUrl_basic() {
        String url = GitHubRequestBuilder.builder()
                .forUserDetails()
                .username("torvalds")
                .build();

        assertEquals("https://api.github.com/users/torvalds", url);
    }
}
