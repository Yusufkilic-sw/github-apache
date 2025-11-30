package com.example.demo.dto;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GithubLicenseDTO {
    private String key;
    private String name;
    
    @SerializedName("spdx_id")
    private String spdxId;
    
    private String url;
    
    @SerializedName("node_id")
    private String nodeId;    
}