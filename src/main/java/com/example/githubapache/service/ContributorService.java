package com.example.githubapache.service;

import java.util.List;
import java.util.Optional;

import com.example.githubapache.entity.ContributorEntity;

/**
 * Service interface for contributor read-only data access operations
 */
public interface ContributorService {
    
    /**
     * Get all contributors
     */
    List<ContributorEntity> getAllContributors();
    
    /**
     * Get contributors by repository name
     */
    List<ContributorEntity> getContributorsByRepositoryName(String repositoryName);
    
    /**
     * Get a contributor by ID
     */
    Optional<ContributorEntity> getContributorById(Long id);
    
    /**
     * Save a contributor
     */
    ContributorEntity saveContributor(ContributorEntity contributor);
    
    /**
     * Get total count of contributors
     */
    long getContributorCount();
}
