package com.schoolmap.entity.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageResultDTO<T> {
    private Long currentPage;
    private Long pageSize;
    private Long totalPages;
    private Long totalNums;
    private List<T> records;

    public PageResultDTO() {}

    public PageResultDTO(Long currentPage, Long pageSize, Long totalNums, List<T> records) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalNums = totalNums;
        this.records = records;
        // 计算总页数
        this.totalPages = (totalNums + pageSize - 1) / pageSize;
    }

    /**
     * 是否有上一页
     */
    public boolean hasPrevious() {
        return currentPage > 1;
    }

    /**
     * 是否有下一页
     */
    public boolean hasNext() {
        return currentPage < totalPages;
    }

    /**
     * 获取起始记录位置
     */
    public Long getStartRecord() {
        return (currentPage - 1) * pageSize + 1;
    }

    /**
     * 获取结束记录位置
     */
    public Long getEndRecord() {
        Long end = currentPage * pageSize;
        return Math.min(end, totalNums);
    }
}
