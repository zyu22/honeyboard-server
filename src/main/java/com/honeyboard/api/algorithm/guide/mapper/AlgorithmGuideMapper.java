package com.honeyboard.api.algorithm.guide.mapper;

import com.honeyboard.api.algorithm.guide.model.AlgorithmGuide;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlgorithmGuideMapper {

    List<AlgorithmGuide> getAlgorithmGuides(int generationId);

    AlgorithmGuide getAlgorithmGuideDetail (@Param("id") int id, @Param("bookmarkflag") boolean bookmarkflag);

    List<AlgorithmGuide> searchAlgorithmGuide(@Param("generationId") int generationId, @Param("title") String title);

    int addAlgorithmGuide (@Param("generationId") int generationId, @Param("algorithmGuide") AlgorithmGuide algorithmGuide);

    int updateAlgorithmGuide(@Param("id") int id, @Param("algorithmGuide") AlgorithmGuide algorithmGuide);

    int deleteAlgorithmGuide(int id);
}
