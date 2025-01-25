package com.honeyboard.api.user.mapper;

import com.honeyboard.api.user.model.mypage.MyAlgorithmSolutionList;
import com.honeyboard.api.user.model.mypage.MyFinaleProjectList;
import com.honeyboard.api.user.model.mypage.MyTrackProjectList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MyPageMapper {

    // 내 트랙프로젝트 조회
    List<MyTrackProjectList> selectAllMyTrackProjects(@Param("userId") int userId);

    // 내 파이널프로젝트 조회
    List<MyFinaleProjectList> selectAllMyFinaleProjects(@Param("userId") int userId);

    // 내 알고리즘 조회
    List<MyAlgorithmSolutionList> selectAllMyAlgorithmSolutions(@Param("userId") int userId);

}
