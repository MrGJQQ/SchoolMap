package com.schoolmap.service.impl;

import com.schoolmap.entity.BuildWorkSpace;
import com.schoolmap.entity.dto.PageResultDTO;
import com.schoolmap.mapper.BuildWorkSpaceMapper;
import com.schoolmap.mapper.BuildingMapper;
import com.schoolmap.service.BuildWorkSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

import java.util.List;
import java.util.Map;

@Service
public class BuildWorkSpaceServiceImpl implements BuildWorkSpaceService {

    @Resource
    private BuildWorkSpaceMapper buildWorkSpaceMapper;

    @Override
    public Boolean insertWorkSpace(BuildWorkSpace workSpace) {
        Integer spaceNums = buildWorkSpaceMapper.countWorkSpace();
        String buildNo = String.format("W%08d", spaceNums);
        workSpace.setSpaceNo(buildNo);
        return buildWorkSpaceMapper.insertWorkSpace(workSpace);
    }

    @Override
    public PageResultDTO<BuildWorkSpace> getWorkSpacesByPage(Long currentPage, Long pageSize, Map<String, Object> conditions) {
        Long offset = (currentPage - 1) * pageSize;
        // 获取分页数据
        List<BuildWorkSpace> buildingList = buildWorkSpaceMapper.getWorkSpacesByPage(offset, pageSize, conditions);
        // 获取总记录数
        Long totalNums = buildWorkSpaceMapper.countWorkSpaceNums(conditions);

        // 使用通用分页工具构建结果
        PageResultDTO<BuildWorkSpace> pageResult = new PageResultDTO<>(currentPage, pageSize, totalNums, buildingList);
        return pageResult;

    }

    @Override
    public Boolean updateWorkSpace(BuildWorkSpace workSpace) {
        return buildWorkSpaceMapper.updateWorkSpace(workSpace);
    }

    @Override
    public Boolean deleteWorkSpacesBatch(List<Integer> ids) {
        return buildWorkSpaceMapper.deleteWorkSpacesByIds(ids);
    }

    @Override
    public List<BuildWorkSpace> listAllWoekspace() {
        return buildWorkSpaceMapper.listAllbuild();
    }

}
