package com.honeyboard.api.bookmark.model;

import com.honeyboard.api.exception.BusinessException;
import com.honeyboard.api.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum ContentType {
    ALGO_GUIDE("알고리즘 가이드"),
    ALGO_SOLUTION("알고리즘 솔루션"),
    WEB_GUIDE("웹 가이드"),
    WEB_RECOMMEND("웹 추천");

    private final String description;

    ContentType(String description) {
        this.description = description;
    }

    public static ContentType from(String type) {
        try {
            return ContentType.valueOf(type.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_CONTENT_TYPE);
        }
    }
}