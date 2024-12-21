package com.honeyboard.api.common.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageInfo {
    private int currentPage;
    private int pageSize;
    private int totalElements;
    private int totalPages;
}
