package com.schoolmap.service;

import com.schoolmap.entity.dto.Neo4jSyncResult;

public interface Neo4jSyncService {

    Neo4jSyncResult syncAllDataToNeo4j();

    Neo4jSyncResult syncBuildingsToNeo4j();

    Neo4jSyncResult syncWorkSpacesToNeo4j();

    Neo4jSyncResult syncWorkContentsToNeo4j();
}