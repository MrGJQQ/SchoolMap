package com.schoolmap.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PathPlanResult {
    private Boolean success;
    private String message;
    private List<SinglePathResult> paths;
    private Integer totalPaths;
}