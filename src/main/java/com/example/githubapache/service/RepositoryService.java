package com.example.githubapache.service;

import java.util.List;
import java.util.Optional;

import com.example.githubapache.entity.RepositoryEntity;

public interface RepositoryService {
    
    List<RepositoryEntity> getAllRepositories();
    
    Optional<RepositoryEntity> getRepositoryById(Long id);

    RepositoryEntity saveRepository(RepositoryEntity repository);

    long getRepositoryCount();
}
