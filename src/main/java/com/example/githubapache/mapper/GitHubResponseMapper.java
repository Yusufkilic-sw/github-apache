package com.example.githubapache.mapper;

import com.example.githubapache.dto.GitHubRepoDto;
import com.example.githubapache.dto.GithubLicenseDTO;
import com.example.githubapache.response.GitHubRepoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GitHubResponseMapper {
    
    @Mapping(target = "license", source = "license")
    GitHubRepoDto toRepoDto(GitHubRepoResponse response);
    
    List<GitHubRepoDto> toRepoDtoList(List<GitHubRepoResponse> responses);
    
    GithubLicenseDTO toLicenseDto(com.example.githubapache.response.GitHubLicenseResponse response);
}
