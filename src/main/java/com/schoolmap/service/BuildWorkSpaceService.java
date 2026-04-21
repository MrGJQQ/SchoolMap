package com.schoolmap.service;

import com.schoolmap.entity.BuildWorkSpace;
import com.schoolmap.entity.dto.PageResultDTO;

import java.util.List;
import java.util.Map;

public interface BuildWorkSpaceService {


    /**
     * 插入工作空间
     */
    Boolean insertWorkSpace(BuildWorkSpace workSpace);

    /**
     * 分页条件查询工作空间
     */
    PageResultDTO<BuildWorkSpace> getWorkSpacesByPage(Long currentPage, Long pageSize, Map<String, Object> conditions);

    /**
     * 更新工作空间
     */
    Boolean updateWorkSpace(BuildWorkSpace workSpace);

    /**
     * 批量删除工作空间
     */
    Boolean deleteWorkSpacesBatch(List<Integer> ids);

    List<BuildWorkSpace> listAllWoekspace();
}
