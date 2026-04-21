package com.schoolmap.mapper;

import com.schoolmap.entity.RoadEdge;
import com.schoolmap.entity.RoadNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoadMapper {

    List<RoadEdge> getRoadEdgesByPage(Long offset, Long pageSize, Map<String, Object> params);
    Long countRoadEdgesNums(Map<String, Object> params);

    List<RoadNode> getRoadNodesByPage(Long offset, Long pageSize, Map<String, Object> params);
    Long countRoadNodesNums(Map<String, Object> params) ;

    Boolean insertRoadEdges(RoadEdge roadEdge) ;
    Boolean insertRoadNodes(RoadNode roadNode) ;

    Boolean updateRoadEdges(RoadEdge roadEdge) ;
    Boolean updateRoadNodes(RoadNode roadNode) ;

    Boolean deleteRoadEdges(List<Integer> roadEdgeIds) ;
    Boolean deleteRoadNodes(List<Integer> roadNodeIds) ;

    List<RoadEdge> listAllRoadEdge();
    List<RoadNode> listAllRoadNode();
}
