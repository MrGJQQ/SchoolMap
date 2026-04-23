package com.schoolmap.service.impl;

import com.schoolmap.entity.Building;
import com.schoolmap.entity.WorkContent;
import com.schoolmap.entity.dto.PageResultDTO;
import com.schoolmap.mapper.WorkContentMapper;
import com.schoolmap.service.WorkContentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WorkContentServiceImpl implements WorkContentService {

    @Resource
    private WorkContentMapper workMapper;

    @Override
    public Boolean insertWork(WorkContent workContent) {

        return workMapper.insertWorkContent(workContent);
    }

    @Override
    public Boolean updateWork(WorkContent workContent) {
        return workMapper.updateWorkContent(workContent);
    }

    @Override
    public Boolean deleteWork(List<Integer> ids) {
        return workMapper.deleteWorkContent(ids);
    }

    @Override
    public PageResultDTO<WorkContent> listWorkContent(Long currentPage, Long pageSize, Map<String, Object> conditions) {
        Long offset = (currentPage - 1) * pageSize;
        // 获取分页数据
        List<WorkContent> workList = workMapper.listWorkContent(offset, pageSize, conditions);
        // 获取总记录数
        Long totalNums = workMapper.countWorkNums(conditions);

        PageResultDTO<WorkContent> pageResult = new PageResultDTO<>(currentPage, pageSize, totalNums, workList);

        return pageResult;
    }
}
