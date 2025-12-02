package com.example.githubapache.service.impl;

import com.example.githubapache.entity.ContributorEntity;
import com.example.githubapache.repository.ContributorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContributorServiceImplTest {

    @InjectMocks
    private ContributorServiceImpl service;

    @Mock
    private ContributorRepository repository;

    @Test
    void getAllContributors_returnsList() {
        when(repository.findAll()).thenReturn(List.of(new ContributorEntity()));
        assertEquals(1, service.getAllContributors().size());
    }

    @Test
    void getContributorsByRepositoryName_returnsList() {
        when(repository.findByRepositoryName("repo")).thenReturn(List.of(new ContributorEntity()));
        assertEquals(1, service.getContributorsByRepositoryName("repo").size());
    }

    @Test
    void saveContributor_saves() {
        ContributorEntity e = new ContributorEntity();
        when(repository.save(e)).thenReturn(e);
        assertSame(e, service.saveContributor(e));
    }

    @Test
    void getContributorCount_returnsCount() {
        when(repository.count()).thenReturn(7L);
        assertEquals(7L, service.getContributorCount());
    }
}
