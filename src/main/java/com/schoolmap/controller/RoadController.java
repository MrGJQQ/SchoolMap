package com.schoolmap.controller;

import com.schoolmap.common.Result;
import com.schoolmap.constants.Constants;
import com.schoolmap.entity.BuildWorkSpace;
import com.schoolmap.entity.RoadEdge;
import com.schoolmap.entity.RoadNode;
import com.schoolmap.entity.dto.PageResultDTO;
import com.schoolmap.service.RoadService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/road")
public class RoadController {

    @Resource
    private RoadService roadService;

    @GetMapping("/getAllRoad")
    public Result getAllRoadEdge() {
        List<RoadEdge> roadEdgeList = roadService.listAllRoadEdge();
        return Result.success(roadEdgeList);
    }
    @GetMapping("/getAllEdge")
    public Result getAllRoadNode() {
        List<RoadNode> roadNodeList = roadService.listAllRoadNode();
        return Result.success(roadNodeList);
    }


    @GetMapping
    public Result pageRoadEdges(@RequestParam(defaultValue = "1") Long currentPage,
                                @RequestParam(defaultValue = "10") Long pageSize,
                                @RequestParam(required = false) String roadNo,
                                @RequestParam(required = false) String roadName,
                                @RequestParam(required = false) Integer roadType,
                                @RequestParam(required = false) String uploader) {
        Map<String, Object> params = new HashMap<>();
        if (roadNo != null && !roadNo.isEmpty()) {
            params.put("roadNo", roadNo);
        }
        if (roadName != null && !roadName.isEmpty()) {
            params.put("roadName", roadName);
        }
        if (roadType != null) {
            params.put("roadType", roadType);
        }
        if (uploader != null && !uploader.isEmpty()) {
            params.put("uploader", uploader);
        }
        PageResultDTO<RoadEdge> result = roadService.pageRoadEdges(currentPage, pageSize, params);
        return Result.success(result);
    }

    @GetMapping("/node")
    public Result pageRoadNodes(@RequestParam(defaultValue = "1") Long currentPage,
                                @RequestParam(defaultValue = "10") Long pageSize,
                                @RequestParam(required = false) String nodeNo,
                                @RequestParam(required = false) String nodeName,
                                @RequestParam(required = false) Integer nodeType,
                                @RequestParam(required = false) Integer belong,
                                @RequestParam(required = false) String uploader) {
        Map<String, Object> params = new HashMap<>();
        if (nodeNo != null && !nodeNo.isEmpty()) {
            params.put("nodeNo", nodeNo);
        }
        if (nodeName != null && !nodeName.isEmpty()) {
            params.put("nodeName", nodeName);
        }
        if (nodeType != null) {
            params.put("nodeType", nodeType);
        }
        if (belong != null) {
            params.put("belong", belong);
        }

        if (uploader != null && !uploader.isEmpty()) {
            params.put("uploader", uploader);
        }
        PageResultDTO<RoadNode> result = roadService.pageRoadNodes(currentPage, pageSize, params);
        return Result.success(result);
    }

    // 新增道路
    @PostMapping("/add")
    public Result addRoadEdges(@RequestBody RoadEdge roadEdge) {
        Boolean result = roadService.insertRoadEdges(roadEdge);
        if (result) {
            return Result.success("添加成功");
        } else {
            return Result.error(Constants.CODE_500, "添加失败");
        }
    }
    @PostMapping("/node/add")
    public Result addRoadNodes(@RequestBody RoadNode roadNode) {
        Boolean result = roadService.insertRoadNodes(roadNode);
        if (result) {
            return Result.success("添加成功");
        } else {
            return Result.error(Constants.CODE_500, "添加失败");
        }
    }

    // 更新道路
    @PostMapping("/update")
    public Result updateRoadEdges(@RequestBody RoadEdge roadEdge) {
        Boolean result = roadService.updateEdges(roadEdge);
        if (result) {
            return Result.success("更新成功");
        } else {
            return Result.error(Constants.CODE_500,"更新失败");
        }
    }
    @PostMapping("/node/update")
    public Result updateRoadNodes(@RequestBody RoadNode roadNode) {
        Boolean result = roadService.updateNodes(roadNode);
        if (result) {
            return Result.success("更新成功");
        } else {
            return Result.error(Constants.CODE_500,"更新失败");
        }
    }

    // 删除道路
    @DeleteMapping("/delete")
    public Result deleteRoadEdges(@RequestBody List<Integer> roadEdgeIds) {
        Boolean result = roadService.deleteRoadEdges(roadEdgeIds);
        if (result) {
            return Result.success("删除成功");
        } else {
            return Result.error(Constants.CODE_500,"删除失败");
        }
    }
    @DeleteMapping("/node/delete")
    public Result deleteRoadNodes(@RequestBody List<Integer> roadNodeIds) {
        Boolean result = roadService.deleteRoadNodes(roadNodeIds);
        if (result) {
            return Result.success("删除成功");
        } else {
            return Result.error(Constants.CODE_500,"删除失败");
        }
    }

}
