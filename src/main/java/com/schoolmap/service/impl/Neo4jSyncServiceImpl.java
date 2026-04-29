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
import java.util.stream.Collectors;

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
        int buildingUpdatedCount = 0;
        int workSpaceUpdatedCount = 0;
        int workContentUpdatedCount = 0;
        int buildingDeletedCount = 0;
        int workSpaceDeletedCount = 0;
        int workContentDeletedCount = 0;

        try {
            // 1. 同步建筑物
            Neo4jSyncResult buildingResult = syncBuildingsToNeo4j();
            buildingCount = buildingResult.getBuildingCount();
            buildingUpdatedCount = buildingResult.getBuildingUpdatedCount();
            buildingDeletedCount = buildingResult.getBuildingDeletedCount();

            // 2. 同步工作空间
            Neo4jSyncResult workSpaceResult = syncWorkSpacesToNeo4j();
            workSpaceCount = workSpaceResult.getWorkSpaceCount();
            workSpaceUpdatedCount = workSpaceResult.getWorkSpaceUpdatedCount();
            workSpaceDeletedCount = workSpaceResult.getWorkSpaceDeletedCount();

            // 3. 同步工作内容
            Neo4jSyncResult workContentResult = syncWorkContentsToNeo4j();
            workContentCount = workContentResult.getWorkContentCount();
            workContentUpdatedCount = workContentResult.getWorkContentUpdatedCount();
            workContentDeletedCount = workContentResult.getWorkContentDeletedCount();

            // 4. 同步关系
            syncRelationships();

            String message = String.format("同步完成: 建筑物 %d 个(更新 %d, 删除 %d), 工作空间 %d 个(更新 %d, 删除 %d), 工作内容 %d 个(更新 %d, 删除 %d)",
                    buildingCount, buildingUpdatedCount, buildingDeletedCount,
                    workSpaceCount, workSpaceUpdatedCount, workSpaceDeletedCount,
                    workContentCount, workContentUpdatedCount, workContentDeletedCount);

            log.info(message);
            log.info("节点关系同步完成");

            return Neo4jSyncResult.builder()
                    .success(true)
                    .buildingCount(buildingCount)
                    .workSpaceCount(workSpaceCount)
                    .workContentCount(workContentCount)
                    .buildingUpdatedCount(buildingUpdatedCount)
                    .workSpaceUpdatedCount(workSpaceUpdatedCount)
                    .workContentUpdatedCount(workContentUpdatedCount)
                    .buildingDeletedCount(buildingDeletedCount)
                    .workSpaceDeletedCount(workSpaceDeletedCount)
                    .workContentDeletedCount(workContentDeletedCount)
                    .message(message)
                    .build();

        } catch (Exception e) {
            log.error("同步数据到Neo4j失败", e);
            return Neo4jSyncResult.builder()
                    .success(false)
                    .buildingCount(buildingCount)
                    .workSpaceCount(workSpaceCount)
                    .workContentCount(workContentCount)
                    .buildingUpdatedCount(buildingUpdatedCount)
                    .workSpaceUpdatedCount(workSpaceUpdatedCount)
                    .workContentUpdatedCount(workContentUpdatedCount)
                    .buildingDeletedCount(buildingDeletedCount)
                    .workSpaceDeletedCount(workSpaceDeletedCount)
                    .workContentDeletedCount(workContentDeletedCount)
                    .message("同步失败: " + e.getMessage())
                    .build();
        }
    }
    @Override
    @Transactional
    public Neo4jSyncResult syncBuildingsToNeo4j() {
        log.info("开始同步建筑物数据到Neo4j");

        int addCount = 0;
        int updateCount = 0;
        int deleteCount = 0;

        try {
            // 查询MySQL中所有未删除的数据
            List<Building> activeBuildings = buildingMapper.listAllbuild();

            // 获取所有未删除的buildNo
            List<String> activeBuildNos = activeBuildings.stream()
                    .map(Building::getBuildNo)
                    .collect(Collectors.toList());

            // 处理每个未删除的建筑物
            for (Building building : activeBuildings) {
                var existingNode = buildingNodeRepository.findFirstByBuildNo(building.getBuildNo());

                BuildingNode node;
                if (existingNode.isPresent()) {
                    node = existingNode.get();
                    updateCount++;
                } else {
                    node = new BuildingNode();
                    addCount++;
                }

                // 设置属性
                node.setId(building.getId());
                node.setBuildNo(building.getBuildNo());
                node.setBuildName(building.getBuildName());
                node.setGis(building.getGis());
                node.setAbout(building.getAbout());
                node.setUploader(building.getUploader());

                buildingNodeRepository.save(node);
            }

            // 查找Neo4j中存在但MySQL中已删除或不存在的节点,直接物理删除
            List<BuildingNode> allNodes = buildingNodeRepository.findAll();
            for (BuildingNode node : allNodes) {
                if (!activeBuildNos.contains(node.getBuildNo())) {
                    buildingNodeRepository.delete(node);
                    deleteCount++;
                    log.warn("建筑物 {} 在MySQL中已被删除或不存在,已从Neo4j中物理删除", node.getBuildNo());
                }
            }

            int totalCount = addCount + updateCount;
            String message = String.format("成功同步建筑物: 新增 %d 个, 更新 %d 个, 删除 %d 个",
                    addCount, updateCount, deleteCount);
            log.info(message);

            return Neo4jSyncResult.builder()
                    .success(true)
                    .buildingCount(totalCount)
                    .buildingUpdatedCount(updateCount)
                    .buildingDeletedCount(deleteCount)
                    .message(message)
                    .build();

        } catch (Exception e) {
            log.error("同步建筑物数据失败", e);
            return Neo4jSyncResult.builder()
                    .success(false)
                    .buildingCount(0)
                    .buildingUpdatedCount(updateCount)
                    .buildingDeletedCount(deleteCount)
                    .message("同步建筑物失败: " + e.getMessage())
                    .build();
        }
    }

    @Override
    @Transactional
    public Neo4jSyncResult syncWorkSpacesToNeo4j() {
        log.info("开始同步工作空间数据到Neo4j");

        int addCount = 0;
        int updateCount = 0;
        int deleteCount = 0;

        try {
            // 查询MySQL中所有未删除的数据
            List<BuildWorkSpace> activeWorkSpaces = buildWorkSpaceMapper.listAllbuild();

            // 获取所有未删除的spaceNo
            List<String> activeSpaceNos = activeWorkSpaces.stream()
                    .map(BuildWorkSpace::getSpaceNo)
                    .collect(Collectors.toList());

            // 处理每个未删除的工作空间
            for (BuildWorkSpace workSpace : activeWorkSpaces) {
                var existingNode = workSpaceNodeRepository.findFirstBySpaceNo(workSpace.getSpaceNo());

                WorkSpaceNode node;
                if (existingNode.isPresent()) {
                    node = existingNode.get();
                    updateCount++;
                } else {
                    node = new WorkSpaceNode();
                    addCount++;
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
            }

            // 查找Neo4j中存在但MySQL中已删除或不存在的节点,直接物理删除
            List<WorkSpaceNode> allNodes = workSpaceNodeRepository.findAll();
            for (WorkSpaceNode node : allNodes) {
                if (!activeSpaceNos.contains(node.getSpaceNo())) {
                    workSpaceNodeRepository.delete(node);
                    deleteCount++;
                    log.warn("工作空间 {} 在MySQL中已被删除或不存在,已从Neo4j中物理删除", node.getSpaceNo());
                }
            }

            int totalCount = addCount + updateCount;
            String message = String.format("成功同步工作空间: 新增 %d 个, 更新 %d 个, 删除 %d 个",
                    addCount, updateCount, deleteCount);
            log.info(message);

            return Neo4jSyncResult.builder()
                    .success(true)
                    .workSpaceCount(totalCount)
                    .workSpaceUpdatedCount(updateCount)
                    .workSpaceDeletedCount(deleteCount)
                    .message(message)
                    .build();

        } catch (Exception e) {
            log.error("同步工作空间数据失败", e);
            return Neo4jSyncResult.builder()
                    .success(false)
                    .workSpaceCount(0)
                    .workSpaceUpdatedCount(updateCount)
                    .workSpaceDeletedCount(deleteCount)
                    .message("同步工作空间失败: " + e.getMessage())
                    .build();
        }
    }

    @Override
    @Transactional
    public Neo4jSyncResult syncWorkContentsToNeo4j() {
        log.info("开始同步工作内容数据到Neo4j");

        int addCount = 0;
        int updateCount = 0;
        int deleteCount = 0;

        try {
            // 查询MySQL中所有未删除的数据
            Map<String, Object> conditions = new HashMap<>();
            List<WorkContent> activeWorkContents = workContentMapper.listWorkContent(0L, 10000L, conditions);

            // 获取所有未删除的id
            List<Integer> activeIds = activeWorkContents.stream()
                    .map(WorkContent::getId)
                    .collect(Collectors.toList());

            // 处理每个未删除的工作内容
            for (WorkContent workContent : activeWorkContents) {
                var existingNode = workContentNodeRepository.findFirstById(workContent.getId());

                WorkContentNode node;
                if (existingNode.isPresent()) {
                    node = existingNode.get();
                    updateCount++;
                } else {
                    node = new WorkContentNode();
                    addCount++;
                }

                // 设置属性
                node.setId(workContent.getId());
                node.setWorkName(workContent.getWorkName());
                node.setAbout(workContent.getAbout());
                node.setSpaceNo(workContent.getSpaceNo());
                node.setSpaceName(workContent.getSpaceName());
                node.setUploader(workContent.getUploader());

                workContentNodeRepository.save(node);
            }

            // 查找Neo4j中存在但MySQL中已删除或不存在的节点,直接物理删除
            List<WorkContentNode> allNodes = workContentNodeRepository.findAll();
            for (WorkContentNode node : allNodes) {
                if (!activeIds.contains(node.getId())) {
                    workContentNodeRepository.delete(node);
                    deleteCount++;
                    log.warn("工作内容 {} 在MySQL中已被删除或不存在,已从Neo4j中物理删除", node.getId());
                }
            }

            int totalCount = addCount + updateCount;
            String message = String.format("成功同步工作内容: 新增 %d 个, 更新 %d 个, 删除 %d 个",
                    addCount, updateCount, deleteCount);
            log.info(message);

            return Neo4jSyncResult.builder()
                    .success(true)
                    .workContentCount(totalCount)
                    .workContentUpdatedCount(updateCount)
                    .workContentDeletedCount(deleteCount)
                    .message(message)
                    .build();

        } catch (Exception e) {
            log.error("同步工作内容数据失败", e);
            return Neo4jSyncResult.builder()
                    .success(false)
                    .workContentCount(0)
                    .workContentUpdatedCount(updateCount)
                    .workContentDeletedCount(deleteCount)
                    .message("同步工作内容失败: " + e.getMessage())
                    .build();
        }
    }

    @Override
    @Transactional
    public void syncRelationships() {
        log.info("开始同步节点关系到Neo4j");

        try {
            // 创建WorkSpace -> Building的关系
            workSpaceNodeRepository.createWorkSpaceToBuildingRelationships();
            log.info("成功创建WorkSpace到Building的关系");

            // 创建WorkContent -> WorkSpace的关系
            workContentNodeRepository.createWorkContentToWorkspaceRelationships();
            log.info("成功创建WorkContent到WorkSpace的关系");

            log.info("节点关系同步完成");

        } catch (Exception e) {
            log.error("同步关系失败", e);
            throw new RuntimeException("同步关系失败: " + e.getMessage(), e);
        }
    }
}
