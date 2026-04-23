package com.schoolmap.entity.neo4j;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("WorkSpace")
public class WorkSpaceNode {

    @Id
    @GeneratedValue
    private Long nodeId;

    private Integer id;

    private String spaceNo;

    private String spaceName;

    private String type;

    private Integer buildId;

    private String about;

    private String uploader;
}