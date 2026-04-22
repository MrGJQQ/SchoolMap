package com.schoolmap.controller;

import com.schoolmap.common.Result;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/pathplan")
public class PathPlanController {


    @GetMapping
    public Result getPathPlan(String beginNo, String endNo, Integer howType){


        return Result.success();
    }


}
