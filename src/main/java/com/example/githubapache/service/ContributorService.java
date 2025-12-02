package com.example.githubapache.service;

import java.util.List;

import com.example.githubapache.entity.ContributorEntity;

public interface ContributorService {
    
    List<ContributorEntity> getAllContributors();

    List<ContributorEntity> getContributorsByRepositoryName(String repositoryName);

    ContributorEntity saveContributor(ContributorEntity contributor);

    long getContributorCount();
}
