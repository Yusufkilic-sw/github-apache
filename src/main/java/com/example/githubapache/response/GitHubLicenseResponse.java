package com.example.githubapache.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GitHubLicenseResponse {
    private String key;
    private String name;
    
    @JsonProperty("spdx_id")
    private String spdxId;
    
    private String url;
    
    @JsonProperty("node_id")
    private String nodeId;    
}
