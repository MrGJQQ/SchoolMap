package com.schoolmap.service;

import com.schoolmap.entity.dto.PathPlanResult;

public interface PathPlanService {

    PathPlanResult planPath(String beginNo, String endNo, Integer howType);
}