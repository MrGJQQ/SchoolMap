package com.schoolmap.service;

import com.schoolmap.entity.WorkContent;
import com.schoolmap.entity.dto.PageResultDTO;

import java.util.List;
import java.util.Map;

public interface WorkContentService {

    Boolean insertWork(WorkContent workContent);

    Boolean updateWork(WorkContent workContent);

    Boolean deleteWork(List<Integer> ids);

    PageResultDTO<WorkContent> listWorkContent(Long currentPage, Long pageSize, Map<String, Object> conditions);
}
