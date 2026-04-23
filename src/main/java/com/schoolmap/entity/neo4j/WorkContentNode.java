package com.schoolmap.entity.neo4j;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("WorkContent")
public class WorkContentNode {

    @Id
    @GeneratedValue
    private Long nodeId;

    private Integer id;

    private String workName;

    private String about;

    private String spaceNo;

    private String spaceName;

    private String uploader;
}