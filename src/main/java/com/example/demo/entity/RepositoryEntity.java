package com.example.demo.entity;

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
    
    @Column(name = "license_key")
    private String licenseKey;
    
    @Column(name = "license_name")
    private String licenseName;
    
    @Column(name = "license_spdx_id")
    private String licenseSpdxId;
    
    @Column(name = "license_url")
    private String licenseUrl;
    
    @Column(name = "license_node_id")
    private String licenseNodeId;
    
    private String url;

    @Column(columnDefinition = "TEXT")
    private String description;

}
