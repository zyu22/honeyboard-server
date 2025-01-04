package com.honeyboard.api.common.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageInfo {
    private int currentPage;
    private int pageSize;
    private int totalElements;
    private int totalPages;

    public PageInfo(int currentPage, int pageSize, int totalElements) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil(totalElements / (double) pageSize);
    }
}
