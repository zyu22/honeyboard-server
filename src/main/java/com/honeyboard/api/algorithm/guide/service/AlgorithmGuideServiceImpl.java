package com.honeyboard.api.algorithm.guide.service;

import com.honeyboard.api.algorithm.guide.mapper.AlgorithmGuideMapper;
import com.honeyboard.api.algorithm.guide.model.AlgorithmGuide;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlgorithmGuideServiceImpl implements AlgorithmGuideService {

    private final AlgorithmGuideMapper algorithmGuideMapper;

    @Override
    public List<AlgorithmGuide> getAlgorithmGuides(int generationId) {
        return algorithmGuideMapper.getAlgorithmGuides(generationId);
    }

    @Override
    public AlgorithmGuide getAlgorithmGuideDetail(int id, boolean bookmarkflag) {
        return algorithmGuideMapper.getAlgorithmGuideDetail(id, bookmarkflag);
    }

    @Override
    public List<AlgorithmGuide> searchAlgorithmGuide(int generationId, String title) {
        return algorithmGuideMapper.searchAlgorithmGuide(generationId, title);
    }

    @Override
    public boolean addAlgorithmGuide(int generationId, AlgorithmGuide algorithmGuide) {
        int result = algorithmGuideMapper.addAlgorithmGuide(generationId, algorithmGuide);
        return result==1;
    }

    @Override
    public boolean updateAlgorithmGuide(int id, AlgorithmGuide algorithmGuide) {
        int result = algorithmGuideMapper.updateAlgorithmGuide(id, algorithmGuide);
        return result==1;
    }

    @Override
    public boolean deleteAlgorithmGuide(int id) {
        int result = algorithmGuideMapper.deleteAlgorithmGuide(id);
        return result==1;
    }
}
