package com.honeyboard.api.project.finale.mapper;

import com.honeyboard.api.project.finale.model.request.FinaleProjectCreate;
import com.honeyboard.api.project.finale.model.request.FinaleProjectUpdate;
import com.honeyboard.api.project.finale.model.response.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public
interface FinaleProjectMapper {

    // 프로젝트 리스트 조회
    List<FinaleProjectList> selectFinaleProjectList();

    // 프로젝트 생성
    int insertFinaleProject(@Param("finaleProjectCreate")FinaleProjectCreate project);

    // 프로젝트 수정
    int updateFinaleProject(@Param("finaleProjectId") int finaleProjectId,
                            @Param("request") FinaleProjectUpdate request);

    // 프로젝트 존재 확인
    boolean checkFinaleProject(@Param("finaleProjectId") int finaleProjectId);

    // 프로젝트 삭제
    int updateFinaleProjectDeleteStatus(int finaleProjectId);

    // 프로젝트 상세 조회
    FinaleProjectDetail selectFinaleProjectDetail(int finaleProjectId);

    int selectLastInsertedId();

}
