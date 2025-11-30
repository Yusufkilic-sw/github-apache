package com.example.demo.controller;

import com.example.demo.entity.RepositoryEntity;
import com.example.demo.entity.ContributorEntity;
import com.example.demo.service.RepositoryService;
import com.example.demo.service.ContributorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Data Management", description = "API endpoints for managing GitHub Apache repositories and contributors data")
public class DataController {

    private final RepositoryService repositoryService;
    private final ContributorService contributorService;

    @GetMapping("/repositories")
    @Operation(summary = "Get all repositories", description = "Retrieve all Apache repositories from the database")
    public List<RepositoryEntity> getAllRepositories() {
        return repositoryService.getAllRepositories();
    }

    @GetMapping("/repositories/{name}/contributors")
    @Operation(summary = "Get contributors by repository", description = "Retrieve all top contributors for a specific repository")
    public List<ContributorEntity> getContributorsByRepository(@PathVariable String name) {
        return contributorService.getContributorsByRepositoryName(name);
    }

    @GetMapping("/contributors")
    @Operation(summary = "Get all contributors", description = "Retrieve all contributors across all repositories")
    public List<ContributorEntity> getAllContributors() {
        return contributorService.getAllContributors();
    }

    @GetMapping("/repositories/count")
    @Operation(summary = "Get repositories count", description = "Get total number of repositories in database")
    public long getRepositoriesCount() {
        return repositoryService.getRepositoryCount();
    }

    @GetMapping("/contributors/count")
    @Operation(summary = "Get contributors count", description = "Get total number of contributors in database")
    public long getContributorsCount() {
        return contributorService.getContributorCount();
    }
}
