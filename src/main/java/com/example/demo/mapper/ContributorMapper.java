package com.example.demo.mapper;

import com.example.demo.dto.GitHubContributorDto;
import com.example.demo.dto.GitHubUserDto;
import com.example.demo.entity.ContributorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ContributorMapper {
    ContributorMapper INSTANCE = Mappers.getMapper(ContributorMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "repositoryName", source = "repositoryName")
    @Mapping(target = "username", source = "contrib.login")
    @Mapping(target = "profileUrl", source = "contrib.url")
    @Mapping(target = "avatarUrl", source = "contrib.avatarUrl")
    @Mapping(target = "contributions", source = "contrib.contributions")
    @Mapping(target = "location", source = "user.location")
    @Mapping(target = "company", source = "user.company")
    ContributorEntity toEntity(GitHubContributorDto contrib, GitHubUserDto user, String repositoryName);
}
