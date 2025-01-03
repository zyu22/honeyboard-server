package com.honeyboard.api.algorithm.guide.mapper;

import com.honeyboard.api.algorithm.guide.model.AlgorithmGuide;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlgorithmGuideMapper {

    List<AlgorithmGuide> getAlgorithmGuides(@Param("offset") int offset, @Param("pageSize") int pageSize, 
    								@Param("generationId") int generationId);

    AlgorithmGuide getAlgorithmGuideDetail (@Param("id") int id, @Param("bookmarkflag") boolean bookmarkflag);

    List<AlgorithmGuide> searchAlgorithmGuide(@Param("offset") int offset, @Param("pageSize") int pageSize, 
    								@Param("generationId") int generationId, 
    								@Param("searchType") String searchType, 
    								@Param("keyword") String keyword);

    int addAlgorithmGuide (@Param("generationId") int generationId, @Param("algorithmGuide") AlgorithmGuide algorithmGuide);

    int updateAlgorithmGuide(@Param("id") int id, @Param("algorithmGuide") AlgorithmGuide algorithmGuide);

    int softDeleteAlgorithmGuide(int id);

    int countAlgorithmGuide(int generationId);

    int countSearchAlgorithmGuide(@Param("generationId") int generationId, @Param("searchType") String searchType, @Param("keyword") String keyword);
}
