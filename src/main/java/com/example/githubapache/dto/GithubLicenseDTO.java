package com.example.githubapache.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GithubLicenseDTO {
    private String key;
    private String name;
    
    private String spdxId;
    
    private String url;
    
    private String nodeId;    
}