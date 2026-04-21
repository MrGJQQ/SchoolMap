package com.schoolmap.controller;

import com.schoolmap.common.Result;
import com.schoolmap.constants.Constants;
import com.schoolmap.entity.BuildWorkSpace;
import com.schoolmap.entity.Building;
import com.schoolmap.entity.dto.PageResultDTO;
import com.schoolmap.service.BuildingService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/build")
public class BuildingController {

    @Resource
    private BuildingService buildingService;

    @GetMapping("/getAbleBuilds")
    public Result overMapBuild(){
        List<Building> buildingList = buildingService.listAllbuild();
        return Result.success(buildingList);
    }

    @GetMapping("/getBuildingChild")
    public Result getBuildingChild(Integer buildId){
        List<BuildWorkSpace> workSpaceList = buildingService.getBuildingChild(buildId);
        return Result.success(workSpaceList);
    }


    @GetMapping("/queryById/{id}")
    public Result queryByBuildId(@PathVariable Integer id) {
        Building building = buildingService.queryById(id);
        return Result.success(building);
    }

    // 分页查询建筑物列表
    @GetMapping
    public Result pageBuildings(@RequestParam(defaultValue = "1") Long currentPage,
                                     @RequestParam(defaultValue = "10") Long pageSize,
                                     @RequestParam(required = false) String buildNo,
                                     @RequestParam(required = false) String buildName,
                                     @RequestParam(required = false) String uploader) {
        Map<String, Object> params = new HashMap<>();
        if (buildNo != null && !buildNo.isEmpty()) {
            params.put("buildNo", buildNo);
        }
        if (buildName != null && !buildName.isEmpty()) {
            params.put("buildName", buildName);
        }
        if (uploader != null && !uploader.isEmpty()) {
            params.put("uploader", uploader);
        }
        PageResultDTO<Building> result = buildingService.pageBuildings(currentPage, pageSize, params);
        return Result.success(result);
    }

    // 新增建筑物
    @PostMapping("/add")
    public Result addBuild(@RequestBody Building building) {
        Boolean result = buildingService.insertBuild(building);
        if (result) {
            return Result.success("添加成功");
        } else {
            return Result.error(Constants.CODE_500, "添加失败");
        }
    }

    // 更新建筑物
    @PostMapping("/update")
    public Result updateBuild(@RequestBody Building building) {
        Boolean result = buildingService.updateBuild(building);
        if (result) {
            return Result.success("更新成功");
        } else {
            return Result.error(Constants.CODE_500,"更新失败");
        }
    }

    // 删除建筑物
    @DeleteMapping("/delete")
    public Result deleteBuild(@RequestBody List<Integer> buildingIds) {
        Boolean result = buildingService.deleteBuild(buildingIds);
        if (result) {
            return Result.success("删除成功");
        } else {
            return Result.error(Constants.CODE_500,"删除失败");
        }
    }

    // 根据建筑物名称模糊查询
    @GetMapping("/queryByBuildName/{buildName}")
    public Result queryByBuildName(@PathVariable String buildName) {
        List<Building> buildings = buildingService.queryByBuildName(buildName);
        return Result.success(buildings);
    }
}
