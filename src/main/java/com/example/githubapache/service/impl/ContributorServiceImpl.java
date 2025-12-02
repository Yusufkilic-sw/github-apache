package com.example.githubapache.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.githubapache.entity.ContributorEntity;
import com.example.githubapache.repository.ContributorRepository;
import com.example.githubapache.service.ContributorService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ContributorServiceImpl implements ContributorService {
    
    private final ContributorRepository contributorRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<ContributorEntity> getAllContributors() {
        log.debug("Fetching all contributors");
        return contributorRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ContributorEntity> getContributorsByRepositoryName(String repositoryName) {
        log.debug("Fetching contributors for repository: {}", repositoryName);
        return contributorRepository.findByRepositoryName(repositoryName);
    }
    
    @Override
    public ContributorEntity saveContributor(ContributorEntity contributor) {
        log.info("Saving contributor: {}", contributor.getUsername());
        return contributorRepository.save(contributor);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getContributorCount() {
        long count = contributorRepository.count();
        log.info("Total contributors count: {}", count);
        return count;
    }
}
