package com.schoolmap.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Neo4jGraphData {

    private List<Neo4jNode> nodes;

    private List<Neo4jRelationship> relationships;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Neo4jNode {
        private String id;
        private List<String> labels;
        private Map<String, Object> properties;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Neo4jRelationship {
        private String id;
        private String type;
        private String startNodeId;
        private String endNodeId;
        private Map<String, Object> properties;
    }
}