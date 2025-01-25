package com.honeyboard.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum ErrorCode {

    // 토큰 관련 에러
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "리프레시 토큰을 찾을 수 없습니다."),

    // 중복 관련 에러
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 등록된 이메일입니다."),
    DUPLICATE_TEAM_MEMBER(HttpStatus.CONFLICT, "이미 등록된 팀원입니다."),
    DUPLICATE_URL(HttpStatus.CONFLICT, "이미 등록된 URL입니다."),
    DUPLICATE_VIDEO(HttpStatus.CONFLICT, "이미 등록된 영상입니다."),
    DUPLICATE_TEAM_LEADER_ID(HttpStatus.CONFLICT, "팀장이 이미 다른 팀에 속해있습니다."),
    DUPLICATE_TEAM_MEMBER_ID(HttpStatus.CONFLICT, "팀원이 이미 다른 팀에 속해있습니다."),
    DUPLICATE_PROBLEM_ID(HttpStatus.CONFLICT, "이미 등록된 문제입니다."),

    // 인증코드 관련 에러
    VERIFICATION_CODE_INVALID(HttpStatus.BAD_REQUEST, "유효하지 않은 인증 코드입니다."),
    VERIFICATION_CODE_EXPIRED(HttpStatus.GONE, "만료된 인증 코드입니다."),


    // 유효성 검증
    INVALID_TEAM_MEMBERS(HttpStatus.BAD_REQUEST, "유효하지 않은 팀원 정보입니다."),
    INVALID_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 사용자 ID가 포함되어 있습니다."),
    INVALID_BOARD_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 게시글 ID입니다."),
    INVALID_PROJECT_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 프로젝트 ID입니다."),
    INVALID_CONTENT_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 컨텐츠 타입입니다."),
    INVALID_CONTENT_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 컨텐츠 ID입니다."),
    INVALID_RPOBLEM_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 문제 ID입니다."),
    INVALID_SOLUTION_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 풀이 ID입니다."),


    // 중복 관련 에러
    UNAUTHORIZED_BOARD_UPDATE(HttpStatus.FORBIDDEN, "게시글 수정 권한이 없습니다."),
    UNAUTHORIZED_WEB_GUIDE_UPDATE(HttpStatus.FORBIDDEN, "게시글 작성 권한이 없습니다."),
    UNAUTHORIZED_TEAM_MEMBER(HttpStatus.FORBIDDEN, "해당 팀의 멤버가 아닙니다."),


    // 프로젝트 관련 에러
    PROJECT_LEADER_REQUIRED(HttpStatus.BAD_REQUEST, "팀 리더는 필수입니다."),
    PROJECT_TITLE_REQUIRED(HttpStatus.BAD_REQUEST, "프로젝트 제목은 필수입니다."),
    PROJECT_DESCRIPTION_REQUIRED(HttpStatus.BAD_REQUEST, "프로젝트 설명은 필수입니다."),
    PROJECT_URL_REQUIRED(HttpStatus.BAD_REQUEST, "프로젝트 URL은 필수입니다."),
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "프로젝트를 찾을 수 없습니다."),
    PROJECT_CREATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "프로젝트 수정에 실패했습니다."),
    PROJECT_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "프로젝트 수정에 실패했습니다."),
    PROJECT_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "프로젝트 삭제에 실패했습니다."),
    PROJECT_TEAM_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "팀 프로젝트 수정에 실패했습니다."),

    // 게시글 관련 에러
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    BOARD_CREATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 작성에 실패했습니다."),
    BOARD_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 수정에 실패했습니다."),
    BOARD_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "게시글 삭제에 실패했습니다."),
    BOARD_TITLE_REQUIRED(HttpStatus.BAD_REQUEST, "게시글 제목은 필수입니다."),
    BOARD_CONTENT_REQUIRED(HttpStatus.BAD_REQUEST, "게시글 내용은 필수입니다."),
    BOARD_EXCLUDED_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "제외 인원 추가에 실패하였습니다."),

    // 팀 관련 에러
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "팀을 찾을 수 없습니다."),
    TEAM_CREATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "팀 생성에 실패하였습니다."),
    TEAM_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "팀 수정에 실패하였습니다."),
    TEAM_INSERT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "팀 인원 변경에 실패하였습니다."),
    INVALID_TEAM_ID(HttpStatus.BAD_REQUEST, "잘못된 팀 아이디를 입력했습니다."),
	
	// 북마크 관련 에러
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "북마크를 찾을 수 없습니다."),
    BOOKMARK_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 북마크입니다."),
    BOOKMARK_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "북마크 삭제에 실패했습니다."),

    // 태그 관련 에러
    TAG_UPDATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "태그 업데이트에 실패하였습니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
