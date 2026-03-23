package com.schoolmap.service.impl;

import com.schoolmap.entity.Building;
import com.schoolmap.entity.dto.PageResultDTO;
import com.schoolmap.mapper.BuildingMapper;
import com.schoolmap.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BuildingServiceImpl implements BuildingService {

    @Autowired
    private BuildingMapper buildingMapper;

    @Override
    public Building queryByBuildNo(String buildNo) {
        return buildingMapper.queryByBuildNo(buildNo);
    }

    @Override
    public Building queryById(Integer id) {
        return buildingMapper.queryById(id);
    }

    @Override
    public List<Building> queryByIdList(List<Integer> ids) {
        return buildingMapper.queryByIdList(ids);
    }

    @Override
    public Boolean insertBuild(Building building) {
        return buildingMapper.insertBuild(building);
    }

    @Override
    public PageResultDTO<Building> pageBuildings(Long currentPage, Long pageSize, Map<String, Object> conditions) {
        Long offset = (currentPage - 1) * pageSize;
        // 获取分页数据
        List<Building> buildingList = buildingMapper.getBuildingsByPage(offset, pageSize, conditions);
        // 获取总记录数
        Long totalNums = buildingMapper.countBuildingsNums(conditions);

        // 使用通用分页工具构建结果
        PageResultDTO<Building> pageResult = new PageResultDTO<>(currentPage, pageSize, totalNums, buildingList);
        return pageResult;
    }

    @Override
    public Boolean updateBuild(Building building) {
        return buildingMapper.updateBuild(building);
    }

    @Override
    public Boolean deleteBuild(List<Integer> buildingIds) {
        return buildingMapper.deleteBuild(buildingIds);
    }

    @Override
    public List<Building> queryByType(String type) {
        return buildingMapper.queryByType(type);
    }

    @Override
    public List<Building> queryByUploader(String uploader) {
        return buildingMapper.queryByUploader(uploader);
    }

    @Override
    public List<Building> queryByBuildName(String buildName) {
        return buildingMapper.queryByBuildName(buildName);
    }

    @Override
    public List<Building> listAllbuild() {
        return buildingMapper.listAllbuild();
    }
}
