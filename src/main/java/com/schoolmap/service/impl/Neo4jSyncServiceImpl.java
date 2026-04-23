package com.schoolmap.service.impl;

import com.schoolmap.entity.BuildWorkSpace;
import com.schoolmap.entity.Building;
import com.schoolmap.entity.WorkContent;
import com.schoolmap.entity.dto.Neo4jSyncResult;
import com.schoolmap.entity.neo4j.BuildingNode;
import com.schoolmap.entity.neo4j.WorkContentNode;
import com.schoolmap.entity.neo4j.WorkSpaceNode;
import com.schoolmap.mapper.BuildWorkSpaceMapper;
import com.schoolmap.mapper.BuildingMapper;
import com.schoolmap.mapper.WorkContentMapper;
import com.schoolmap.repository.BuildingNodeRepository;
import com.schoolmap.repository.WorkContentNodeRepository;
import com.schoolmap.repository.WorkSpaceNodeRepository;
import com.schoolmap.service.Neo4jSyncService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class Neo4jSyncServiceImpl implements Neo4jSyncService {

    @Resource
    private BuildingMapper buildingMapper;

    @Resource
    private BuildWorkSpaceMapper buildWorkSpaceMapper;

    @Resource
    private WorkContentMapper workContentMapper;

    @Resource
    private BuildingNodeRepository buildingNodeRepository;

    @Resource
    private WorkSpaceNodeRepository workSpaceNodeRepository;

    @Resource
    private WorkContentNodeRepository workContentNodeRepository;

    @Override
    @Transactional
    public Neo4jSyncResult syncAllDataToNeo4j() {
        log.info("开始同步所有数据到Neo4j");

        int buildingCount = 0;
        int workSpaceCount = 0;
        int workContentCount = 0;

        try {
            // 1. 同步建筑物
            Neo4jSyncResult buildingResult = syncBuildingsToNeo4j();
            buildingCount = buildingResult.getBuildingCount();

            // 2. 同步工作空间
            Neo4jSyncResult workSpaceResult = syncWorkSpacesToNeo4j();
            workSpaceCount = workSpaceResult.getWorkSpaceCount();

            // 3. 同步工作内容
            Neo4jSyncResult workContentResult = syncWorkContentsToNeo4j();
            workContentCount = workContentResult.getWorkContentCount();

            String message = String.format("同步完成: 建筑物 %d 个, 工作空间 %d 个, 工作内容 %d 个",
                    buildingCount, workSpaceCount, workContentCount);

            log.info(message);

            return Neo4jSyncResult.builder()
                    .success(true)
                    .buildingCount(buildingCount)
                    .workSpaceCount(workSpaceCount)
                    .workContentCount(workContentCount)
                    .message(message)
                    .build();

        } catch (Exception e) {
            log.error("同步数据到Neo4j失败", e);
            return Neo4jSyncResult.builder()
                    .success(false)
                    .buildingCount(buildingCount)
                    .workSpaceCount(workSpaceCount)
                    .workContentCount(workContentCount)
                    .message("同步失败: " + e.getMessage())
                    .build();
        }
    }

    @Override
    @Transactional
    public Neo4jSyncResult syncBuildingsToNeo4j() {
        log.info("开始同步建筑物数据到Neo4j");

        int count = 0;
        try {
            // 查询所有未删除的建筑物
            Map<String, Object> conditions = new HashMap<>();
            List<Building> buildings = buildingMapper.getBuildingsByPage(0L, 10000L, conditions);

            for (Building building : buildings) {
                // 检查是否已存在
                var existingNode = buildingNodeRepository.findFirstByBuildNo(building.getBuildNo());

                BuildingNode node;
                if (existingNode.isPresent()) {
                    node = existingNode.get();
                } else {
                    node = new BuildingNode();
                }

                // 设置属性
                node.setId(building.getId());
                node.setBuildNo(building.getBuildNo());
                node.setBuildName(building.getBuildName());
                node.setGis(building.getGis());
                node.setAbout(building.getAbout());
                node.setUploader(building.getUploader());

                buildingNodeRepository.save(node);
                count++;
            }

            String message = String.format("成功同步 %d 个建筑物到Neo4j", count);
            log.info(message);

            return Neo4jSyncResult.builder()
                    .success(true)
                    .buildingCount(count)
                    .message(message)
                    .build();

        } catch (Exception e) {
            log.error("同步建筑物数据失败", e);
            return Neo4jSyncResult.builder()
                    .success(false)
                    .buildingCount(count)
                    .message("同步建筑物失败: " + e.getMessage())
                    .build();
        }
    }

    @Override
    @Transactional
    public Neo4jSyncResult syncWorkSpacesToNeo4j() {
        log.info("开始同步工作空间数据到Neo4j");

        int count = 0;
        try {
            // 查询所有未删除的工作空间
            List<BuildWorkSpace> workSpaces = buildWorkSpaceMapper.listAllbuild();

            for (BuildWorkSpace workSpace : workSpaces) {
                // 检查是否已存在
                var existingNode = workSpaceNodeRepository.findFirstBySpaceNo(workSpace.getSpaceNo());

                WorkSpaceNode node;
                if (existingNode.isPresent()) {
                    node = existingNode.get();
                } else {
                    node = new WorkSpaceNode();
                }

                // 设置属性
                node.setId(workSpace.getId());
                node.setSpaceNo(workSpace.getSpaceNo());
                node.setSpaceName(workSpace.getSpaceName());
                node.setType(workSpace.getType());
                node.setBuildId(workSpace.getBuildId());
                node.setAbout(workSpace.getAbout());
                node.setUploader(workSpace.getUploader());

                workSpaceNodeRepository.save(node);
                count++;
            }

            String message = String.format("成功同步 %d 个工作空间到Neo4j", count);
            log.info(message);

            return Neo4jSyncResult.builder()
                    .success(true)
                    .workSpaceCount(count)
                    .message(message)
                    .build();

        } catch (Exception e) {
            log.error("同步工作空间数据失败", e);
            return Neo4jSyncResult.builder()
                    .success(false)
                    .workSpaceCount(count)
                    .message("同步工作空间失败: " + e.getMessage())
                    .build();
        }
    }

    @Override
    @Transactional
    public Neo4jSyncResult syncWorkContentsToNeo4j() {
        log.info("开始同步工作内容数据到Neo4j");

        int count = 0;
        try {
            // 查询所有未删除的工作内容
            Map<String, Object> conditions = new HashMap<>();
            List<WorkContent> workContents = workContentMapper.listWorkContent(0L, 10000L, conditions);

            for (WorkContent workContent : workContents) {
                // 检查是否已存在
                var existingNode = workContentNodeRepository.findFirstById(workContent.getId());

                WorkContentNode node;
                if (existingNode.isPresent()) {
                    node = existingNode.get();
                } else {
                    node = new WorkContentNode();
                }

                // 设置属性
                node.setId(workContent.getId());
                node.setWorkName(workContent.getWorkName());
                node.setAbout(workContent.getAbout());
                node.setSpaceNo(workContent.getSpaceNo());
                node.setSpaceName(workContent.getSpaceName());
                node.setUploader(workContent.getUploader());

                workContentNodeRepository.save(node);
                count++;
            }

            String message = String.format("成功同步 %d 个工作内容到Neo4j", count);
            log.info(message);

            return Neo4jSyncResult.builder()
                    .success(true)
                    .workContentCount(count)
                    .message(message)
                    .build();

        } catch (Exception e) {
            log.error("同步工作内容数据失败", e);
            return Neo4jSyncResult.builder()
                    .success(false)
                    .workContentCount(count)
                    .message("同步工作内容失败: " + e.getMessage())
                    .build();
        }
    }
}