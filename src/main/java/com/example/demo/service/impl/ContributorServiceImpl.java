package com.example.demo.service.impl;

import com.example.demo.entity.ContributorEntity;
import com.example.demo.repository.ContributorRepository;
import com.example.demo.service.ContributorService;
import com.example.demo.util.AppLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of ContributorService interface
 * Provides access to contributor data with logging and transaction management
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ContributorServiceImpl implements ContributorService {
    
    private final ContributorRepository contributorRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<ContributorEntity> getAllContributors() {
        AppLogger.LOGGER.info("Fetching all contributors");
        return contributorRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ContributorEntity> getContributorsByRepositoryName(String repositoryName) {
        AppLogger.LOGGER.info("Fetching contributors for repository: " + repositoryName);
        return contributorRepository.findByRepositoryName(repositoryName);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<ContributorEntity> getContributorById(Long id) {
        AppLogger.LOGGER.info("Fetching contributor by ID: " + id);
        return contributorRepository.findById(id);
    }
    
    @Override
    public ContributorEntity saveContributor(ContributorEntity contributor) {
        AppLogger.LOGGER.info("Saving contributor: " + contributor.getUsername());
        return contributorRepository.save(contributor);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getContributorCount() {
        long count = contributorRepository.count();
        AppLogger.LOGGER.info("Total contributors count: " + count);
        return count;
    }
}
