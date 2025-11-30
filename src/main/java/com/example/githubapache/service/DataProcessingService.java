package com.example.githubapache.service;

/**
 * Service interface for data processing operations
 */
public interface DataProcessingService {
    
    /**
     * Process Apache repositories: fetch top 5 by stars,
     * get top 10 contributors for each, and persist to database
     */
    void processApacheRepositories();
}
