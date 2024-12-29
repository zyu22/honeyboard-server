package com.honeyboard.api.project.track.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TeamRole {
    LEADER, MEMBER;

    // String을 TeamRole로 변환 (대소문자 구분 없이)
    public static TeamRole fromString(String text) {
        if (text != null) {
            for (TeamRole role : TeamRole.values()) {
                if (text.equalsIgnoreCase(role.name())) {
                    return role;
                }
            }
        }
        return MEMBER; // 기본값
    }

    // DB의 ENUM('leader', 'member')과 매칭하기 위한 메소드
    @JsonValue  // JSON 직렬화 시 소문자로 변환
    public String getValue() {
        return this.name().toLowerCase();
    }
}
