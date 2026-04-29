package com.schoolmap.service;

import com.schoolmap.entity.dto.Neo4jGraphData;

public interface Neo4jGraphService {

    Neo4jGraphData getFullGraph();

    Neo4jGraphData getBuildingSubGraph(String buildNo);

    Neo4jGraphData getWorkSpaceSubGraph(String spaceNo);

    Neo4jGraphData searchGraph(String keyword);
}