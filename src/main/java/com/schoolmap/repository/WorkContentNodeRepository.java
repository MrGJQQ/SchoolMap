package com.schoolmap.repository;

import com.schoolmap.entity.neo4j.WorkContentNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkContentNodeRepository extends Neo4jRepository<WorkContentNode, Long> {

    List<WorkContentNode> findBySpaceNo(String spaceNo);

    Optional<WorkContentNode> findFirstById(Integer id);
}