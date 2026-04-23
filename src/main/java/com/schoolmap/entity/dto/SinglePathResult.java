package com.schoolmap.entity.dto;

import com.schoolmap.entity.RoadEdge;
import com.schoolmap.entity.RoadNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SinglePathResult {
    private Integer pathIndex;
    private List<RoadNode> pathNodes;
    private List<RoadEdge> pathEdges;
    private Double totalDistance;
    private String distanceUnit;
    private String description;
}