package com.example.githubapache.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "repositories")
public class RepositoryEntity {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer stargazerCount;
    private Integer watchersCount;
    private String language;
    private Integer openIssuesCount;

    @Embedded
    private License license;
    
    private String url;

    @Column(columnDefinition = "TEXT")
    private String description;

}
