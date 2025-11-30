package com.example.demo.service.impl;

import com.example.demo.entity.RepositoryEntity;
import com.example.demo.repository.RepositoryRepository;
import com.example.demo.service.RepositoryService;
import com.example.demo.util.AppLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of RepositoryService interface
 * Provides access to repository data with logging and transaction management
 */
@Service
@Transactional
@RequiredArgsConstructor
public class RepositoryServiceImpl implements RepositoryService {
    
    private final RepositoryRepository repositoryRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<RepositoryEntity> getAllRepositories() {
        AppLogger.LOGGER.info("Fetching all repositories");
        return repositoryRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<RepositoryEntity> getRepositoryByName(String name) {
        AppLogger.LOGGER.info("Fetching repository by name: " + name);
        return repositoryRepository.findByName(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<RepositoryEntity> getRepositoryById(Long id) {
        AppLogger.LOGGER.info("Fetching repository by ID: " + id);
        return repositoryRepository.findById(id);
    }
    
    @Override
    public RepositoryEntity saveRepository(RepositoryEntity repository) {
        AppLogger.LOGGER.info("Saving repository: " + repository.getName());
        return repositoryRepository.save(repository);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getRepositoryCount() {
        long count = repositoryRepository.count();
        AppLogger.LOGGER.info("Total repositories count: " + count);
        return count;
    }
}
