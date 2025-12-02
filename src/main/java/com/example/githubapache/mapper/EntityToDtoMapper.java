package com.example.githubapache.mapper;

import com.example.githubapache.dto.ContributorResponseDto;
import com.example.githubapache.dto.RepositoryResponseDto;
import com.example.githubapache.entity.ContributorEntity;
import com.example.githubapache.entity.RepositoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EntityToDtoMapper {
    
    @Mapping(target = "licenseKey", source = "license.key")
    @Mapping(target = "licenseName", source = "license.name")
    RepositoryResponseDto toRepositoryDto(RepositoryEntity entity);
    
    List<RepositoryResponseDto> toRepositoryDtoList(List<RepositoryEntity> entities);
    
    ContributorResponseDto toContributorDto(ContributorEntity entity);
    
    List<ContributorResponseDto> toContributorDtoList(List<ContributorEntity> entities);
}
