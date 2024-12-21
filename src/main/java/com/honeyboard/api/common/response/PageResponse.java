package com.honeyboard.api.common.response;

import com.honeyboard.api.common.model.PageInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageResponse<T> {
    private List<T> content;
    private PageInfo pageInfo;
}
