package com.honeyboard.api.common.response;

import com.honeyboard.api.common.model.PageInfo;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageResponse<T> {
    private List<T> content;
    private PageInfo pageInfo;
}
