package com.schoolmap.service.impl;

import com.schoolmap.entity.dto.Neo4jGraphData;
import com.schoolmap.service.Neo4jGraphService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class Neo4jGraphServiceImpl implements Neo4jGraphService {

    @Resource
    private Neo4jClient neo4jClient;

    @Override
    public Neo4jGraphData getFullGraph() {
        log.info("从Neo4j获取完整图谱数据");

        try {
            String nodeCypher = "MATCH (n) RETURN n";
            String relCypher = "MATCH ()-[r]->() RETURN r";

            Collection<Map<String, Object>> nodeMaps = neo4jClient.query(nodeCypher).fetch().all();
            Collection<Map<String, Object>> relMaps = neo4jClient.query(relCypher).fetch().all();

            List<Neo4jGraphData.Neo4jNode> nodes = new ArrayList<>();
            for (Map<String, Object> row : nodeMaps) {
                for (Object value : row.values()) {
                    if (value instanceof Node) {
                        nodes.add(convertNode((Node) value));
                    }
                }
            }

            List<Neo4jGraphData.Neo4jRelationship> relationships = new ArrayList<>();
            for (Map<String, Object> row : relMaps) {
                for (Object value : row.values()) {
                    if (value instanceof Relationship) {
                        relationships.add(convertRelationship((Relationship) value));
                    }
                }
            }

            log.info("获取图谱数据成功: {} 个节点, {} 条关系", nodes.size(), relationships.size());

            return Neo4jGraphData.builder()
                    .nodes(nodes)
                    .relationships(relationships)
                    .build();

        } catch (Exception e) {
            log.error("从Neo4j获取图谱数据失败", e);
            return Neo4jGraphData.builder()
                    .nodes(Collections.emptyList())
                    .relationships(Collections.emptyList())
                    .build();
        }
    }

    @Override
    public Neo4jGraphData getBuildingSubGraph(String buildNo) {
        log.info("获取建筑物子图, buildNo: {}", buildNo);

        try {
            String cypher = "MATCH (b:Building {buildNo: $buildNo})<-[:BELONGS_TO_BUILDING]-(ws:WorkSpace)<-[:BELONGS_TO_WORKSPACE]-(wc:WorkContent) " +
                          "RETURN b, ws, wc";

            Collection<Map<String, Object>> records = neo4jClient.query(cypher)
                    .bind(buildNo).to("buildNo")
                    .fetch()
                    .all();

            Set<String> seenNodeIds = new HashSet<>();
            List<Neo4jGraphData.Neo4jNode> nodes = new ArrayList<>();

            for (Map<String, Object> row : records) {
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    Object value = entry.getValue();
                    if (value instanceof Node) {
                        Node node = (Node) value;
                        String nodeId = String.valueOf(node.id());
                        if (!seenNodeIds.contains(nodeId)) {
                            nodes.add(convertNode(node));
                            seenNodeIds.add(nodeId);
                        }
                    }
                }
            }

            List<Neo4jGraphData.Neo4jRelationship> relationships = getRelationshipsForNodes(nodes);

            log.info("获取建筑物子图成功: {} 个节点, {} 条关系", nodes.size(), relationships.size());

            return Neo4jGraphData.builder()
                    .nodes(nodes)
                    .relationships(relationships)
                    .build();

        } catch (Exception e) {
            log.error("获取建筑物子图失败", e);
            return Neo4jGraphData.builder()
                    .nodes(Collections.emptyList())
                    .relationships(Collections.emptyList())
                    .build();
        }
    }

    @Override
    public Neo4jGraphData getWorkSpaceSubGraph(String spaceNo) {
        log.info("获取工作空间子图, spaceNo: {}", spaceNo);

        try {
            String cypher = "MATCH (ws:WorkSpace {spaceNo: $spaceNo}) " +
                          "OPTIONAL MATCH (ws)-[:BELONGS_TO_BUILDING]->(b:Building) " +
                          "OPTIONAL MATCH (ws)<-[:BELONGS_TO_WORKSPACE]-(wc:WorkContent) " +
                          "RETURN ws, b, wc";

            Collection<Map<String, Object>> records = neo4jClient.query(cypher)
                    .bind(spaceNo).to("spaceNo")
                    .fetch()
                    .all();

            Set<String> seenNodeIds = new HashSet<>();
            List<Neo4jGraphData.Neo4jNode> nodes = new ArrayList<>();

            for (Map<String, Object> row : records) {
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    Object value = entry.getValue();
                    if (value instanceof Node) {
                        Node node = (Node) value;
                        String nodeId = String.valueOf(node.id());
                        if (!seenNodeIds.contains(nodeId)) {
                            nodes.add(convertNode(node));
                            seenNodeIds.add(nodeId);
                        }
                    }
                }
            }

            List<Neo4jGraphData.Neo4jRelationship> relationships = getRelationshipsForNodes(nodes);

            log.info("获取工作空间子图成功: {} 个节点, {} 条关系", nodes.size(), relationships.size());

            return Neo4jGraphData.builder()
                    .nodes(nodes)
                    .relationships(relationships)
                    .build();

        } catch (Exception e) {
            log.error("获取工作空间子图失败", e);
            return Neo4jGraphData.builder()
                    .nodes(Collections.emptyList())
                    .relationships(Collections.emptyList())
                    .build();
        }
    }

    @Override
    public Neo4jGraphData searchGraph(String keyword) {
        log.info("搜索图谱, keyword: {}", keyword);

        try {
            String cypher = "MATCH (n) WHERE n.buildName CONTAINS $keyword OR n.spaceName CONTAINS $keyword OR n.workName CONTAINS $keyword RETURN n";

            Collection<Map<String, Object>> records = neo4jClient.query(cypher)
                    .bind(keyword).to("keyword")
                    .fetch()
                    .all();

            List<Neo4jGraphData.Neo4jNode> nodes = new ArrayList<>();
            for (Map<String, Object> row : records) {
                for (Object value : row.values()) {
                    if (value instanceof Node) {
                        nodes.add(convertNode((Node) value));
                    }
                }
            }

            log.info("搜索图谱成功: {} 个节点", nodes.size());

            return Neo4jGraphData.builder()
                    .nodes(nodes)
                    .relationships(Collections.emptyList())
                    .build();

        } catch (Exception e) {
            log.error("搜索图谱失败", e);
            return Neo4jGraphData.builder()
                    .nodes(Collections.emptyList())
                    .relationships(Collections.emptyList())
                    .build();
        }
    }

    private Neo4jGraphData.Neo4jNode convertNode(Node node) {
        if (node == null) return null;

        Map<String, Object> properties = new HashMap<>();
        for (String key : node.keys()) {
            properties.put(key, node.get(key).asObject());
        }

        List<String> labels = new ArrayList<>();
        node.labels().forEach(labels::add);

        return new Neo4jGraphData.Neo4jNode(
                String.valueOf(node.id()),
                labels,
                properties
        );
    }

    private Neo4jGraphData.Neo4jRelationship convertRelationship(Relationship rel) {
        if (rel == null) return null;

        Map<String, Object> properties = new HashMap<>();
        for (String key : rel.keys()) {
            properties.put(key, rel.get(key).asObject());
        }

        return Neo4jGraphData.Neo4jRelationship.builder()
                .id(String.valueOf(rel.id()))
                .type(rel.type())
                .startNodeId(String.valueOf(rel.startNodeId()))
                .endNodeId(String.valueOf(rel.endNodeId()))
                .properties(properties)
                .build();
    }

    private List<Neo4jGraphData.Neo4jRelationship> getRelationshipsForNodes(List<Neo4jGraphData.Neo4jNode> nodes) {
        List<Neo4jGraphData.Neo4jRelationship> relationships = new ArrayList<>();

        if (nodes == null || nodes.isEmpty()) {
            return relationships;
        }

        try {
            List<Long> nodeIds = nodes.stream()
                    .map(node -> Long.parseLong(node.getId()))
                    .collect(Collectors.toList());

            String cypher = "MATCH (n1)-[r]->(n2) WHERE ID(n1) IN $nodeIds AND ID(n2) IN $nodeIds RETURN r";

            Collection<Map<String, Object>> records = neo4jClient.query(cypher)
                    .bind(nodeIds).to("nodeIds")
                    .fetch()
                    .all();

            for (Map<String, Object> row : records) {
                for (Object value : row.values()) {
                    if (value instanceof Relationship) {
                        relationships.add(convertRelationship((Relationship) value));
                    }
                }
            }

        } catch (Exception e) {
            log.error("获取关系失败", e);
        }

        return relationships;
    }
}
