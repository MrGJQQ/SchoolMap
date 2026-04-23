package com.schoolmap.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Neo4jSyncResult {

    private boolean success;

    private int buildingCount;

    private int workSpaceCount;

    private int workContentCount;

    private String message;
}