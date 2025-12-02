package com.example.githubapache.service.impl;

import com.example.githubapache.entity.RepositoryEntity;
import com.example.githubapache.repository.RepositoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepositoryServiceImplTest {

    @InjectMocks
    private RepositoryServiceImpl service;

    @Mock
    private RepositoryRepository repository;

    @Test
    void getAllRepositories_returnsList() {
        when(repository.findAll()).thenReturn(List.of(new RepositoryEntity()));
        assertEquals(1, service.getAllRepositories().size());
    }

    @Test
    void getRepositoryById_null_returnsEmpty() {
        assertTrue(service.getRepositoryById(null).isEmpty());
        verify(repository, never()).findById(anyLong());
    }

    @Test
    void getRepositoryById_returnsOptional() {
        when(repository.findById(1L)).thenReturn(Optional.of(new RepositoryEntity()));
        assertTrue(service.getRepositoryById(1L).isPresent());
    }

    @Test
    void saveRepository_saves() {
        RepositoryEntity e = new RepositoryEntity();
        when(repository.save(e)).thenReturn(e);
        assertSame(e, service.saveRepository(e));
    }

    @Test
    void getRepositoryCount_returnsCount() {
        when(repository.count()).thenReturn(5L);
        assertEquals(5L, service.getRepositoryCount());
    }
}
