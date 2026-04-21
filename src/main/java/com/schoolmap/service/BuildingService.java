package com.schoolmap.service;

import com.schoolmap.entity.BuildWorkSpace;
import com.schoolmap.entity.Building;
import com.schoolmap.entity.dto.PageResultDTO;

import java.util.List;
import java.util.Map;

public interface BuildingService {

    List<Building> listAllbuild();

    List<BuildWorkSpace> getBuildingChild(Integer id);

    Building queryById(Integer id);

    Building queryByBuildNo(String buildNo);

    List<Building> queryByIdList(List<Integer> ids);

    Boolean insertBuild(Building building);

    PageResultDTO<Building> pageBuildings(Long currentPage, Long pageSize, Map<String, Object> params);

    Boolean updateBuild(Building building);

    Boolean deleteBuild(List<Integer> buildingIds);

    List<Building> queryByType(String type);

    List<Building> queryByUploader(String uploader);

    List<Building> queryByBuildName(String buildName);

}
