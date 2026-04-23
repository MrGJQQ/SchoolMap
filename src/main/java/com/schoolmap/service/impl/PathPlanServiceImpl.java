package com.schoolmap.service.impl;

import com.schoolmap.entity.RoadEdge;
import com.schoolmap.entity.RoadNode;
import com.schoolmap.entity.dto.PathPlanResult;
import com.schoolmap.service.PathPlanService;
import com.schoolmap.service.RoadService;
import com.schoolmap.utils.KShortestPathUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PathPlanServiceImpl implements PathPlanService {

    @Resource
    private RoadService roadService;

    @Override
    public PathPlanResult planPath(String beginNo, String endNo, Integer howType) {
        if (beginNo == null || beginNo.isEmpty()) {
            return PathPlanResult.builder()
                    .success(false)
                    .message("起始节点不能为空")
                    .build();
        }

        if (endNo == null || endNo.isEmpty()) {
            return PathPlanResult.builder()
                    .success(false)
                    .message("终止节点不能为空")
                    .build();
        }

        if (howType == null || howType < 0 || howType > 2) {
            return PathPlanResult.builder()
                    .success(false)
                    .message("出行方式参数错误，0-驾车，1-骑行，2-步行")
                    .build();
        }

        List<RoadNode> allNodes = roadService.listAllRoadNode();
        List<RoadEdge> allEdges = roadService.listAllRoadEdge();

        if (allNodes == null || allNodes.isEmpty()) {
            return PathPlanResult.builder()
                    .success(false)
                    .message("路网数据为空")
                    .build();
        }

        if (allEdges == null || allEdges.isEmpty()) {
            return PathPlanResult.builder()
                    .success(false)
                    .message("道路数据为空")
                    .build();
        }

        return KShortestPathUtils.findKShortestPaths(allNodes, allEdges, beginNo, endNo, howType);
    }
}