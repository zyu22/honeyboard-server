package com.honeyboard.api.common.response;

import com.honeyboard.api.common.model.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PageResponse<T> {
    private List<T> content;
    private PageInfo pageInfo;
}
