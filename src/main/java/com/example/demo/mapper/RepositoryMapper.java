package com.example.demo.mapper;

import com.example.demo.dto.GitHubRepoDto;
import com.example.demo.entity.RepositoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RepositoryMapper {
    RepositoryMapper INSTANCE = Mappers.getMapper(RepositoryMapper.class);

    @Mapping(target = "stargazerCount", source = "stargazersCount")
    @Mapping(target = "openIssuesCount", source = "openIssuesCount")
    @Mapping(target = "license.key", source = "license.key")
    @Mapping(target = "license.name", source = "license.name")
    @Mapping(target = "license.spdxId", source = "license.spdxId")
    @Mapping(target = "license.url", source = "license.url")
    @Mapping(target = "license.nodeId", source = "license.nodeId")
    RepositoryEntity toEntity(GitHubRepoDto dto);
}
