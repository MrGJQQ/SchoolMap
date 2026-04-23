package com.schoolmap.repository;

import com.schoolmap.entity.neo4j.BuildingNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuildingNodeRepository extends Neo4jRepository<BuildingNode, Long> {

    Optional<BuildingNode> findFirstByBuildNo(String buildNo);

    Optional<BuildingNode> findFirstById(Integer id);

    void deleteByBuildNo(String buildNo);
}