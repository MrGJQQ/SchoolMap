package com.schoolmap.controller;

import com.schoolmap.common.Result;
import com.schoolmap.entity.dto.PathPlanResult;
import com.schoolmap.service.PathPlanService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/pathplan")
public class PathPlanController {

    @Resource
    private PathPlanService pathPlanService;

    @GetMapping
    public Result getPathPlan(String beginNo, String endNo, Integer howType){
        PathPlanResult result = pathPlanService.planPath(beginNo, endNo, howType);
        return Result.success(result);
    }

}
