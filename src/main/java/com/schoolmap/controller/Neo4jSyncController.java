package com.schoolmap.controller;

import com.schoolmap.common.Result;
import com.schoolmap.entity.dto.Neo4jSyncResult;
import com.schoolmap.service.Neo4jSyncService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/neo4j")
public class Neo4jSyncController {

    @Resource
    private Neo4jSyncService neo4jSyncService;

    @PostMapping("/sync/all")
    public Result syncAllData() {
        log.info("接收到同步所有数据的请求");
        Neo4jSyncResult result = neo4jSyncService.syncAllDataToNeo4j();
        if (result.isSuccess()) {
            return Result.success(result);
        } else {
            return Result.error("500", result.getMessage());
        }
    }

    @PostMapping("/sync/buildings")
    public Result syncBuildings() {
        log.info("接收到同步建筑物数据的请求");
        Neo4jSyncResult result = neo4jSyncService.syncBuildingsToNeo4j();
        if (result.isSuccess()) {
            return Result.success(result);
        } else {
            return Result.error("500", result.getMessage());
        }
    }

    @PostMapping("/sync/workspaces")
    public Result syncWorkSpaces() {
        log.info("接收到同步工作空间数据的请求");
        Neo4jSyncResult result = neo4jSyncService.syncWorkSpacesToNeo4j();
        if (result.isSuccess()) {
            return Result.success(result);
        } else {
            return Result.error("500", result.getMessage());
        }
    }

    @PostMapping("/sync/workcontents")
    public Result syncWorkContents() {
        log.info("接收到同步工作内容数据的请求");
        Neo4jSyncResult result = neo4jSyncService.syncWorkContentsToNeo4j();
        if (result.isSuccess()) {
            return Result.success(result);
        } else {
            return Result.error("500", result.getMessage());
        }
    }
}