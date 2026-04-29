package com.schoolmap.mapper;

import com.schoolmap.entity.BuildWorkSpace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface BuildWorkSpaceMapper{

    List<BuildWorkSpace> getWorkSpacesByPage(@Param("offset") Long offset,
                                           @Param("pageSize") Long pageSize,
                                           @Param("conditions") Map<String, Object> conditions);

    Long countWorkSpaceNums(@Param("conditions") Map<String, Object> conditions);


    @Select("SELECT * FROM location_workspace WHERE space_name LIKE CONCAT('%', #{spaceName}, '%')")
    List<BuildWorkSpace> queryBySpaceName(String spaceName);

    Boolean deleteWorkSpacesByIds(List<Integer> ids);

    Boolean insertWorkSpace(BuildWorkSpace workSpace);

    Boolean updateWorkSpace(BuildWorkSpace workSpace);

    List<BuildWorkSpace> listAllbuild();

    Integer countWorkSpace();

    List<BuildWorkSpace> getSpaceListByBuildId(Integer id);

    BuildWorkSpace  queryBySpaceNo(String spaceNo);

}
