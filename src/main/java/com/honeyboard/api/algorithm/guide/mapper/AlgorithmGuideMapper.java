package com.honeyboard.api.algorithm.guide.mapper;

import com.honeyboard.api.algorithm.guide.model.AlgorithmGuide;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AlgorithmGuideMapper {

    List<AlgorithmGuide> getAlgorithmGuides(int generationId);

    AlgorithmGuide getAlgorithmGuideDetail (int id, boolean bookmarkflag);

    List<AlgorithmGuide> searchAlgorithmGuide(int generationId, String title);

    int addAlgorithmGuide (int generationId, AlgorithmGuide algorithmGuide);

    int updateAlgorithmGuide(int id, AlgorithmGuide algorithmGuide);

    int deleteAlgorithmGuide(int id);
}
