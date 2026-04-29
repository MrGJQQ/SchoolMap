package com.schoolmap.repository;

import com.schoolmap.entity.dto.Neo4jGraphData;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface Neo4jGraphRepository {

    @Query("MATCH (n) RETURN DISTINCT n")
    List<Map<String, Object>> getAllNodes();

    @Query("MATCH ()-[r]->() RETURN DISTINCT r")
    List<Map<String, Object>> getAllRelationships();

    @Query("MATCH (n:Building) RETURN n")
    List<Map<String, Object>> getBuildingNodes();

    @Query("MATCH (n:WorkSpace) RETURN n")
    List<Map<String, Object>> getWorkSpaceNodes();

    @Query("MATCH (n:WorkContent) RETURN n")
    List<Map<String, Object>> getWorkContentNodes();

    @Query("MATCH (n:Building)<-[:BELONGS_TO_BUILDING]-(ws:WorkSpace)<-[:BELONGS_TO_WORKSPACE]-(wc:WorkContent) RETURN n, ws, wc")
    List<Map<String, Object>> getFullGraphWithPath();

    @Query("MATCH (b:Building {buildNo: $buildNo})<-[:BELONGS_TO_BUILDING]-(ws:WorkSpace)<-[:BELONGS_TO_WORKSPACE]-(wc:WorkContent) RETURN b, ws, wc")
    List<Map<String, Object>> getBuildingSubGraph(String buildNo);

    @Query("MATCH (ws:WorkSpace {spaceNo: $spaceNo})<-[:BELONGS_TO_WORKSPACE]-(wc:WorkContent) OPTIONAL MATCH (ws)-[:BELONGS_TO_BUILDING]->(b:Building) RETURN ws, wc, b")
    List<Map<String, Object>> getWorkSpaceSubGraph(String spaceNo);

    @Query("MATCH (n) WHERE n.buildName CONTAINS $keyword OR n.spaceName CONTAINS $keyword OR n.workName CONTAINS $keyword RETURN n")
    List<Map<String, Object>> searchNodes(String keyword);
}