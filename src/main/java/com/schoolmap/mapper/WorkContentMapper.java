package com.schoolmap.mapper;

import com.schoolmap.entity.WorkContent;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WorkContentMapper {

    List<WorkContent> listWorkContent(Long offset, Long pageSize, Map<String, Object> conditions);
    Long countWorkNums(Map<String, Object> conditions);

    Boolean insertWorkContent(WorkContent workContent);
    Boolean updateWorkContent(WorkContent workContent);
    Boolean deleteWorkContent(List<Integer> ids);

}
