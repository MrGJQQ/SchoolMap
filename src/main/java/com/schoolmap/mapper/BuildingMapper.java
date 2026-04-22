package com.schoolmap.mapper;

import com.schoolmap.entity.Building;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface BuildingMapper {

    @Select("select * from location_build where build_no=#{buildNo} and deleted_at IS NULL")
    Building queryByBuildNo(String buildNo);

    Building queryById(Integer id);

    List<Building> queryByIdList(List<Integer> ids);

    Boolean insertBuild(Building building);

    List<Building> getBuildingsByPage(Long offset, Long pageSize, Map<String, Object> conditions);

    Long countBuildingsNums(Map<String, Object> conditions);
    Integer countBuildings();

    Boolean updateBuild(Building building);

    Boolean deleteBuild(List<Integer> buildingIds);

    // 根据类型查询位置
    @Select("select * from location_build where type=#{type} and deleted_at IS NULL")
    List<Building> queryByType(String type);

    // 根据上传者查询位置
    @Select("select * from location_build where uploader=#{uploader} and deleted_at IS NULL")
    List<Building> queryByUploader(String uploader);

    // 根据建筑物名称模糊查询
    @Select("select * from location_build where build_name LIKE CONCAT('%', #{buildName}, '%') and deleted_at IS NULL")
    List<Building> queryByBuildName(String buildName);

    List<Building> listAllbuild();


}
