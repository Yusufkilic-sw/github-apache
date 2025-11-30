package com.example.githubapache.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.githubapache.entity.RepositoryEntity;
import com.example.githubapache.repository.RepositoryRepository;
import com.example.githubapache.service.RepositoryService;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of RepositoryService interface
 * Provides access to repository data with logging and transaction management
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RepositoryServiceImpl implements RepositoryService {
    
    private final RepositoryRepository repositoryRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<RepositoryEntity> getAllRepositories() {
        log.info("Fetching all repositories");
        return repositoryRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<RepositoryEntity> getRepositoryByName(String name) {
        log.info("Fetching repository by name: {}", name);
        return repositoryRepository.findByName(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<RepositoryEntity> getRepositoryById(Long id) {
        log.info("Fetching repository by ID: {}", id);
        return repositoryRepository.findById(id);
    }
    
    @Override
    public RepositoryEntity saveRepository(RepositoryEntity repository) {
        log.info("Saving repository: {}", repository.getName());
        return repositoryRepository.save(repository);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getRepositoryCount() {
        long count = repositoryRepository.count();
        log.info("Total repositories count: {}", count);
        return count;
    }
}
