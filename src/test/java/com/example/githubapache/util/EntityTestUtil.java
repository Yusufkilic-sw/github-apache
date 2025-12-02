package com.example.githubapache.util;

import com.example.githubapache.entity.ContributorEntity;
import com.example.githubapache.entity.RepositoryEntity;

public final class EntityTestUtil {
    private EntityTestUtil() {}

    public static RepositoryEntity repositoryEntity(String name, int stars) {
        RepositoryEntity e = new RepositoryEntity();
        e.setName(name);
        e.setStargazerCount(stars);;
        return e;
    }

    public static ContributorEntity contributorEntity(String repoName, String username) {
        ContributorEntity e = new ContributorEntity();
        e.setRepositoryName(repoName);
        e.setUsername(username);
        return e;
    }

    public static RepositoryEntity getRepositoryEntity() {
        return repositoryEntity("test-repo", 100);
    }

    public static ContributorEntity getContributorEntity() {
        return contributorEntity("test-repo", "test-user");
    }
}
