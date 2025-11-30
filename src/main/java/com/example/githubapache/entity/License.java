package com.example.githubapache.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class License {

    @Column(name = "license_key")
    private String key;

    @Column(name = "license_name")
    private String name;

    @Column(name = "license_spdx_id")
    private String spdxId;

    @Column(name = "license_url")
    private String url;

    @Column(name = "license_node_id")
    private String nodeId;
}
