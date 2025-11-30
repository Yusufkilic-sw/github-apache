package com.example.githubapache.service;

import java.util.List;
import java.util.Optional;

import com.example.githubapache.entity.RepositoryEntity;

/**
 * Service interface for repository read-only data access operations
 */
public interface RepositoryService {
    
    /**
     * Get all repositories
     */
    List<RepositoryEntity> getAllRepositories();
    
    /**
     * Get a repository by name
     */
    Optional<RepositoryEntity> getRepositoryByName(String name);
    
    /**
     * Get a repository by ID
     */
    Optional<RepositoryEntity> getRepositoryById(Long id);
    
    /**
     * Save a repository
     */
    RepositoryEntity saveRepository(RepositoryEntity repository);
    
    /**
     * Get total count of repositories
     */
    long getRepositoryCount();
}
