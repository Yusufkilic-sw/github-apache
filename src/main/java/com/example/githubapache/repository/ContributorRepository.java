package com.example.githubapache.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.githubapache.entity.ContributorEntity;

import java.util.List;

@Repository
public interface ContributorRepository extends JpaRepository<ContributorEntity, Long> {
    List<ContributorEntity> findByRepositoryName(String repositoryName);
}
