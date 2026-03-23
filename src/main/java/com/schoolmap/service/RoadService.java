package com.schoolmap.service;

import com.schoolmap.entity.RoadEdge;
import com.schoolmap.entity.RoadNode;
import com.schoolmap.entity.dto.PageResultDTO;

import java.util.List;
import java.util.Map;

public interface RoadService {

    PageResultDTO<RoadEdge> pageRoadEdges(Long currentPage, Long pageSize, Map<String, Object> params);
    PageResultDTO<RoadNode> pageRoadNodes(Long currentPage, Long pageSize, Map<String, Object> params);

    Boolean insertRoadEdges(RoadEdge roadEdge);
    Boolean insertRoadNodes(RoadNode roadNode);

    Boolean updateEdges(RoadEdge roadEdge);
    Boolean updateNodes(RoadNode roadNode);

    Boolean deleteRoadEdges(List<Integer> roadEdgeIds);
    Boolean deleteRoadNodes(List<Integer> roadNodeIds);
}
