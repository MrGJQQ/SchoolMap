package com.schoolmap.controller;

import com.schoolmap.common.Result;
import com.schoolmap.entity.dto.Neo4jGraphData;
import com.schoolmap.service.Neo4jGraphService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/neo4j/graph")
public class Neo4jGraphController {

    @Resource
    private Neo4jGraphService neo4jGraphService;

    @GetMapping("/full")
    public Result getFullGraph() {
        log.info("获取Neo4j完整图谱");
        Neo4jGraphData graphData = neo4jGraphService.getFullGraph();
        return Result.success(graphData);
    }

    @GetMapping("/building/{buildNo}")
    public Result getBuildingGraph(@PathVariable String buildNo) {
        log.info("获取建筑物图谱, buildNo: {}", buildNo);
        Neo4jGraphData graphData = neo4jGraphService.getBuildingSubGraph(buildNo);
        return Result.success(graphData);
    }

    @GetMapping("/workspace/{spaceNo}")
    public Result getWorkSpaceGraph(@PathVariable String spaceNo) {
        log.info("获取工作空间图谱, spaceNo: {}", spaceNo);
        Neo4jGraphData graphData = neo4jGraphService.getWorkSpaceSubGraph(spaceNo);
        return Result.success(graphData);
    }

    @GetMapping("/search")
    public Result searchGraph(@RequestParam String keyword) {
        log.info("搜索图谱, keyword: {}", keyword);
        Neo4jGraphData graphData = neo4jGraphService.searchGraph(keyword);
        return Result.success(graphData);
    }
}