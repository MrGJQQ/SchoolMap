package com.schoolmap.controller;

import com.schoolmap.annotation.Authority;
import com.schoolmap.common.Result;
import com.schoolmap.constants.Constants;
import com.schoolmap.entity.AuthorityType;
import com.schoolmap.entity.BuildWorkSpace;
import com.schoolmap.entity.dto.PageResultDTO;
import com.schoolmap.service.BuildWorkSpaceService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/workspace")
public class BuildWorkSpaceController {

    @Resource
    private BuildWorkSpaceService buildWorkSpaceService;

    /**
     * 分页查询工作空间列表
     */
    @GetMapping
    public Result getWorkSpacesByPage(@RequestParam(defaultValue = "1") Long currentPage,
                                    @RequestParam(defaultValue = "10") Long pageSize,
                                    @RequestParam(required = false) String spaceNo,
                                    @RequestParam(required = false) String spaceName,
                                    @RequestParam(required = false) String type,
                                    @RequestParam(required = false) Integer buildId,
                                    @RequestParam(required = false) String uploader) {
        Map<String, Object> conditions = new HashMap<>();
        if (spaceNo != null && !spaceNo.isEmpty()) {
            conditions.put("spaceNo", spaceNo);
        }
        if (spaceName != null && !spaceName.isEmpty()) {
            conditions.put("spaceName", spaceName);
        }
        if (type != null && !type.isEmpty()) {
            conditions.put("type", type);
        }
        if (buildId != null) {
            conditions.put("buildId", buildId);
        }
        if (uploader != null && !uploader.isEmpty()) {
            conditions.put("uploader", uploader);
        }
        PageResultDTO<BuildWorkSpace> pageResult = buildWorkSpaceService.getWorkSpacesByPage(currentPage, pageSize, conditions);
        
        return Result.success(pageResult);
    }

    /**
     * 新增工作空间
     */
    @PostMapping("/add")
    @Authority(AuthorityType.requireAuthority)
    public Result addWorkSpace(@RequestBody BuildWorkSpace workSpace) {
        Boolean result = buildWorkSpaceService.insertWorkSpace(workSpace);
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
    @Authority(AuthorityType.requireAuthority)
    public Result updateWorkSpace(@RequestBody BuildWorkSpace workSpace) {
        Boolean result = buildWorkSpaceService.updateWorkSpace(workSpace);
        if (result) {
            return Result.success("更新成功");
        } else {
            return Result.error(Constants.CODE_500, "更新失败");
        }
    }

    /**
     * 批量删除工作空间
     */
    @PostMapping("/batchDelete")
    @Authority(AuthorityType.requireAuthority)
    public Result deleteWorkSpacesBatch(@RequestBody List<Integer> ids) {
        Boolean result = buildWorkSpaceService.deleteWorkSpacesBatch(ids);
        if (result) {
            return Result.success("删除成功");
        } else {
            return Result.error(Constants.CODE_500, "删除失败");
        }
    }
}
