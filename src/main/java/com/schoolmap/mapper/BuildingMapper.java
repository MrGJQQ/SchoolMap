package com.schoolmap.mapper;

import com.schoolmap.entity.Building;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface BuildingMapper {

    @Select("select * from location_build where build_no=#{buildNo}")
    Building queryByBuildNo(String buildNo);

    Building queryById(Integer id);

    List<Building> queryByIdList(List<Integer> ids);

    Boolean insertBuild(Building building);

    List<Building> getBuildingsByPage(Long offset, Long pageSize, Map<String, Object> conditions);

    Long countBuildingsNums(Map<String, Object> conditions);
    Integer countBuildings();

    Boolean updateBuild(Building building);

    Boolean deleteBuild(List<Integer> buildingIds);

    @Select("select * from location_build where type=#{type}")
    List<Building> queryByType(String type);

    @Select("select * from location_build where uploader=#{uploader}")
    List<Building> queryByUploader(String uploader);

    @Select("select * from location_build where build_name LIKE CONCAT('%', #{buildName}, '%')")
    List<Building> queryByBuildName(String buildName);

    List<Building> listAllbuild();

    @Select("select * from location_build")
    List<Building> listAllBuildWithDeleted();

}
