package com.schoolmap.repository;

import com.schoolmap.entity.neo4j.WorkSpaceNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkSpaceNodeRepository extends Neo4jRepository<WorkSpaceNode, Long> {

    List<WorkSpaceNode> findByBuildId(Integer buildId);

    Optional<WorkSpaceNode> findFirstBySpaceNo(String spaceNo);

    Optional<WorkSpaceNode> findFirstById(Integer id);
}