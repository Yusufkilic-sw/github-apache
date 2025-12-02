package com.example.githubapache.controller;

import com.example.githubapache.entity.ContributorEntity;
import com.example.githubapache.entity.RepositoryEntity;
import com.example.githubapache.service.ContributorService;
import com.example.githubapache.service.RepositoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DataControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RepositoryService repositoryService;

    @Mock
    private ContributorService contributorService;

    @InjectMocks
    private DataController controller;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getAllRepositories_returnsList() throws Exception {
        RepositoryEntity repo = new RepositoryEntity();
        repo.setId(1L);
        repo.setName("r1");
        when(repositoryService.getAllRepositories()).thenReturn(List.of(repo));

        mockMvc.perform(get("/api/repositories").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("r1"));
    }

    @Test
    void getContributorsByRepository_returnsList() throws Exception {
        ContributorEntity c = new ContributorEntity();
        c.setId(1L);
        c.setRepositoryName("r1");
        c.setUsername("u1");
        when(contributorService.getContributorsByRepositoryName("r1")).thenReturn(List.of(c));

        mockMvc.perform(get("/api/repositories/{name}/contributors", "r1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("u1"));
    }

    @Test
    void getAllContributors_returnsList() throws Exception {
        ContributorEntity c = new ContributorEntity();
        c.setId(1L);
        c.setRepositoryName("r1");
        c.setUsername("u1");
        when(contributorService.getAllContributors()).thenReturn(List.of(c));

        mockMvc.perform(get("/api/contributors").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].repositoryName").value("r1"));
    }

    @Test
    void getRepositoriesCount_returnsNumber() throws Exception {
        when(repositoryService.getRepositoryCount()).thenReturn(3L);
        mockMvc.perform(get("/api/repositories/count").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }

    @Test
    void getContributorsCount_returnsNumber() throws Exception {
        when(contributorService.getContributorCount()).thenReturn(5L);
        mockMvc.perform(get("/api/contributors/count").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }
}
