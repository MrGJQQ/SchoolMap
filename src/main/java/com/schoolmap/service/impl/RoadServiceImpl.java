package com.schoolmap.service.impl;

import com.schoolmap.entity.RoadEdge;
import com.schoolmap.entity.RoadNode;
import com.schoolmap.entity.dto.PageResultDTO;
import com.schoolmap.mapper.RoadMapper;
import com.schoolmap.service.RoadService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoadServiceImpl implements RoadService {
    @Resource
    private RoadMapper roadMapper;

    public RoadServiceImpl(RoadMapper roadMapper) {
        this.roadMapper = roadMapper;
    }

    @Override
    public PageResultDTO<RoadEdge> pageRoadEdges(Long currentPage, Long pageSize, Map<String, Object> params) {
        Long offset = (currentPage - 1) * pageSize;
        // 获取分页数据
        List<RoadEdge> roadEdgeList = roadMapper.getRoadEdgesByPage(offset, pageSize, params);
        // 获取总记录数
        Long totalNums = roadMapper.countRoadEdgesNums(params);

        // 使用通用分页工具构建结果
        PageResultDTO<RoadEdge> pageResult = new PageResultDTO<>(currentPage, pageSize, totalNums, roadEdgeList);
        return pageResult;
    }

    @Override
    public PageResultDTO<RoadNode> pageRoadNodes(Long currentPage, Long pageSize, Map<String, Object> params) {
        Long offset = (currentPage - 1) * pageSize;
        // 获取分页数据
        List<RoadNode> roadEdgeList = roadMapper.getRoadNodesByPage(offset, pageSize, params);
        // 获取总记录数
        Long totalNums = roadMapper.countRoadNodesNums(params);

        // 使用通用分页工具构建结果
        PageResultDTO<RoadNode> pageResult = new PageResultDTO<>(currentPage, pageSize, totalNums, roadEdgeList);
        return pageResult;
    }

    @Override
    public Boolean insertRoadEdges(RoadEdge roadEdge) {
        Integer edgeNums = countRoadEdges();
        String roadNo = String.format("R%08d", edgeNums);
        roadEdge.setRoadNo(roadNo);
        return roadMapper.insertRoadEdges(roadEdge);
    }
    private Integer countRoadEdges() {
        return roadMapper.countRoadEdges();
    }

    @Override
    public Boolean insertRoadNodes(RoadNode roadNode) {
        Integer nodeNums = countRoadNodes();
        String nodeNo = String.format("N%08d", nodeNums);
        roadNode.setNodeNo(nodeNo);
        return roadMapper.insertRoadNodes(roadNode);
    }
    private Integer countRoadNodes() {
        return roadMapper.countRoadNodes();
    }

    @Override
    public Boolean updateEdges(RoadEdge roadEdge) {
        return roadMapper.updateRoadEdges(roadEdge);
    }

    @Override
    public Boolean updateNodes(RoadNode roadNode) {
        return roadMapper.updateRoadNodes(roadNode);
    }

    @Override
    public Boolean deleteRoadEdges(List<Integer> roadEdgeIds) {
        return roadMapper.deleteRoadEdges(roadEdgeIds);
    }

    @Override
    public Boolean deleteRoadNodes(List<Integer> roadNodeIds) {
        return roadMapper.deleteRoadNodes(roadNodeIds);
    }

    @Override
    public List<RoadEdge> listAllRoadEdge() {
        return roadMapper.listAllRoadEdge();
    }

    @Override
    public List<RoadNode> listAllRoadNode() {
        return roadMapper.listAllRoadNode();
    }

    @Override
    public List<RoadNode> getNodeByName(String nodeName) {
        return roadMapper.getNodeByName(nodeName);
    }
}
