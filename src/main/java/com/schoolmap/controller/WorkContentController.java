package com.schoolmap.controller;

import com.schoolmap.common.Result;
import com.schoolmap.constants.Constants;
import com.schoolmap.entity.WorkContent;
import com.schoolmap.entity.dto.PageResultDTO;
import com.schoolmap.service.WorkContentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/workcontent")
public class WorkContentController {

    @Resource
    private WorkContentService workContentService;

    @RequestMapping()
    public Result getWorkContent(@RequestParam(defaultValue = "1") Long currentPage,
                                 @RequestParam(defaultValue = "10") Long pageSize,
                                 @RequestParam(required = false) String spaceNo,
                                 @RequestParam(required = false) String spaceName,
                                 @RequestParam(required = false) String workName,
                                 @RequestParam(required = false) String uploader){
        Map<String, Object> conditions = new HashMap<>();

        if (spaceNo != null && !spaceNo.isEmpty()) {
            conditions.put("spaceNo", spaceNo);
        }
        if (spaceName != null && !spaceName.isEmpty()) {
            conditions.put("spaceName", spaceName);
        }
        if (workName != null && !workName.isEmpty()) {
            conditions.put("workName", workName);
        }
        if (uploader != null && !uploader.isEmpty()) {
            conditions.put("uploader", uploader);
        }

        PageResultDTO<WorkContent> workContentList = workContentService.listWorkContent(currentPage, pageSize, conditions);
        return Result.success(workContentList);
    }

    @PostMapping("/add")
    public Result addWorkSpace(@RequestBody WorkContent workContent) {
        Boolean result = workContentService.insertWork(workContent);
        if (result) {
            return Result.success("添加成功");
        } else {
            return Result.error(Constants.CODE_500, "添加失败");
        }
    }

    /**
     * 更新工作空间
     */
    @PostMapping("/update")
    public Result updateWorkSpace(@RequestBody WorkContent workContent) {
        Boolean result = workContentService.updateWork(workContent);
        if (result) {
            return Result.success("更新成功");
        } else {
            return Result.error(Constants.CODE_500, "更新失败");
        }
    }

    /**
     * 批量删除工作空间
     */
    @DeleteMapping("/delete")
    public Result deleteWorkSpacesBatch(@RequestBody List<Integer> ids) {
        Boolean result = workContentService.deleteWork(ids);
        if (result) {
            return Result.success("删除成功");
        } else {
            return Result.error(Constants.CODE_500, "删除失败");
        }
    }
}
