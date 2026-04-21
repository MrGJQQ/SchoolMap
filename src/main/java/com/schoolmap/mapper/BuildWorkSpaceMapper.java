package com.schoolmap.mapper;

import com.schoolmap.entity.BuildWorkSpace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface BuildWorkSpaceMapper{

    /**
     * 分页查询工作空间
     */
    List<BuildWorkSpace> getWorkSpacesByPage(@Param("offset") Long offset,
                                           @Param("pageSize") Long pageSize,
                                           @Param("conditions") Map<String, Object> conditions);

    /**
     * 统计工作空间数量
     */
    Long countWorkSpaceNums(@Param("conditions") Map<String, Object> conditions);


    /**
     * 根据场地名称模糊查询
     */
    @Select("SELECT * FROM location_workspace WHERE space_name LIKE CONCAT('%', #{spaceName}, '%') AND deleted_at IS NULL")
    List<BuildWorkSpace> queryBySpaceName(String spaceName);

    Boolean deleteWorkSpacesByIds(List<Integer> ids);

    Boolean insertWorkSpace(BuildWorkSpace workSpace);

    Boolean updateWorkSpace(BuildWorkSpace workSpace);

    List<BuildWorkSpace> listAllbuild();

    List<BuildWorkSpace> getSpaceListByBuildId(Integer id);
}
