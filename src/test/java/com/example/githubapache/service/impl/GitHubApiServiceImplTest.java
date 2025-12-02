package com.example.githubapache.service.impl;

import com.example.githubapache.client.GitHubApiClient;
import com.example.githubapache.dto.GitHubContributorDto;
import com.example.githubapache.dto.GitHubRepoDto;
import com.example.githubapache.dto.GitHubUserDto;
import com.example.githubapache.mapper.GitHubResponseMapper;
import com.example.githubapache.response.GitHubContributorResponse;
import com.example.githubapache.response.GitHubRepoResponse;
import com.example.githubapache.response.GitHubUserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.example.githubapache.util.GitHubResponseTestUtil.*;
import static com.example.githubapache.util.GitHubDtoTestUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GitHubApiServiceImplTest {

    @InjectMocks
    private GitHubApiServiceImpl service;

    @Mock
    private GitHubApiClient client;

    @Mock
    private GitHubResponseMapper mapper;

    @Test
    void getLatestRepositories_success_mapsResponses() {
        List<GitHubRepoResponse> responses = List.of(getRepoResponse());
        when(client.getOrgRepositories(anyString(), anyInt(), anyString(), anyString())).thenReturn(responses);
        List<GitHubRepoDto> mapped = List.of(getRepoDto());
        when(mapper.toRepoDtoList(responses)).thenReturn(mapped);

        List<GitHubRepoDto> result = service.getLatestRepositories(100);

        assertEquals(1, result.size());
        verify(client, times(1)).getOrgRepositories(anyString(), eq(100), eq("updated"), eq("desc"));
        verify(mapper, times(1)).toRepoDtoList(responses);
    }

    @Test
    void getLatestRepositories_clientThrows_returnsEmpty() {
        when(client.getOrgRepositories(anyString(), anyInt(), anyString(), anyString())).thenThrow(new RuntimeException("boom"));

        List<GitHubRepoDto> result = service.getLatestRepositories(100);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getContributors_success_mapsFields() {
        GitHubContributorResponse r = getContributorResponse();
        r.setAvatarUrl("avatar");
        r.setProfileUrl("html");
        r.setUrl("api");
        when(client.getRepositoryContributors(anyString(), eq("test-repo"), anyInt(), anyInt())).thenReturn(List.of(r));

        List<GitHubContributorDto> result = service.getContributors("test-repo");

        assertEquals(1, result.size());
        GitHubContributorDto dto = result.get(0);
        assertEquals(r.getLogin(), dto.getLogin());
        assertEquals(r.getContributions(), dto.getContributions());
        assertEquals("avatar", dto.getAvatarUrl());
        assertEquals("html", dto.getProfileUrl());
        assertEquals("api", dto.getUrl());
    }

    @Test
    void getContributors_clientThrows_returnsEmpty() {
        when(client.getRepositoryContributors(anyString(), anyString(), anyInt(), anyInt())).thenThrow(new RuntimeException("x"));
        List<GitHubContributorDto> result = service.getContributors("repo");
        assertTrue(result.isEmpty());
    }

    @Test
    void getLatestRepositories_nullResponse_returnsEmpty() {
        when(client.getOrgRepositories(anyString(), anyInt(), anyString(), anyString())).thenReturn(null);
        List<GitHubRepoDto> result = service.getLatestRepositories(50);
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mapper, never()).toRepoDtoList(any());
    }

    @Test
    void getContributors_nullResponse_returnsEmpty() {
        when(client.getRepositoryContributors(anyString(), anyString(), anyInt(), anyInt())).thenReturn(null);
        List<GitHubContributorDto> result = service.getContributors("any");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getUserDetails_success_mapsFields() {
        GitHubUserResponse u = getUserResponse();
        u.setProfileUrl("html");
        when(client.getUserDetails("test-user")).thenReturn(u);

        GitHubUserDto dto = service.getUserDetails("test-user");
        assertEquals(u.getLogin(), dto.getLogin());
        assertEquals(u.getLocation(), dto.getLocation());
        assertEquals(u.getCompany(), dto.getCompany());
        assertEquals(u.getName(), dto.getName());
        assertEquals("html", dto.getProfileUrl());
    }

    @Test
    void getUserDetails_clientThrows_returnsDefaultDto() {
        when(client.getUserDetails("x")).thenThrow(new RuntimeException("x"));
        GitHubUserDto dto = service.getUserDetails("x");
        assertNotNull(dto);
        assertNull(dto.getLogin());
    }

    @Test
    void getUserDetails_nullResponse_returnsDefaultDto() {
        when(client.getUserDetails("null-user")).thenReturn(null);
        GitHubUserDto dto = service.getUserDetails("null-user");
        assertNotNull(dto);
        assertNull(dto.getLogin());
        assertNull(dto.getProfileUrl());
    }
}
