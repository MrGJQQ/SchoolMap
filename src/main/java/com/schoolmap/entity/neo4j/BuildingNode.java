package com.schoolmap.entity.neo4j;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("Building")
public class BuildingNode {

    @Id
    @GeneratedValue
    private Long nodeId;

    private Integer id;

    private String buildNo;

    private String buildName;

    private String gis;

    private String about;

    private String uploader;
}