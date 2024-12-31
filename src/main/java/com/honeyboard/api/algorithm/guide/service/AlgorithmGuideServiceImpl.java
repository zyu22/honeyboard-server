package com.honeyboard.api.algorithm.guide.service;

import com.honeyboard.api.algorithm.guide.mapper.AlgorithmGuideMapper;
import com.honeyboard.api.algorithm.guide.model.AlgorithmGuide;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlgorithmGuideServiceImpl implements AlgorithmGuideService {

    private final AlgorithmGuideMapper algorithmGuideMapper;

    @Override
    public List<AlgorithmGuide> getAlgorithmGuides(int generationId) {
        if (generationId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 기수 ID입니다.");
        }
        return algorithmGuideMapper.getAlgorithmGuides(generationId);
    }

    @Override
    public AlgorithmGuide getAlgorithmGuideDetail(int id, boolean bookmarkflag) {
        if (id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 가이드 ID입니다.");
        }
        return algorithmGuideMapper.getAlgorithmGuideDetail(id, bookmarkflag);
    }

    @Override
    public List<AlgorithmGuide> searchAlgorithmGuide(int generationId, String title) {
        if (generationId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 기수 ID입니다.");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("검색어를 입력해주세요.");
        }
        return algorithmGuideMapper.searchAlgorithmGuide(generationId, title);
    }

    @Override
    public boolean addAlgorithmGuide(int generationId, AlgorithmGuide algorithmGuide) {
        validateGuide(algorithmGuide);
        if (generationId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 기수 ID입니다.");
        }

        try {
            int result = algorithmGuideMapper.addAlgorithmGuide(generationId, algorithmGuide);
            if (result != 1) {
                throw new RuntimeException("알고리즘 가이드 등록에 실패했습니다.");
            }
            return true;
        } catch (Exception e) {
            log.error("알고리즘 가이드 등록 실패 - 기수: {}, 오류: {}", generationId, e.getMessage());
            throw new RuntimeException("알고리즘 가이드 등록 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public boolean updateAlgorithmGuide(int id, AlgorithmGuide algorithmGuide) {
        validateGuide(algorithmGuide);
        if (id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 가이드 ID입니다.");
        }

        try {
            int result = algorithmGuideMapper.updateAlgorithmGuide(id, algorithmGuide);
            if (result != 1) {
                throw new RuntimeException("알고리즘 가이드 수정에 실패했습니다.");
            }
            return true;
        } catch (Exception e) {
            log.error("알고리즘 가이드 수정 실패 - ID: {}, 오류: {}", id, e.getMessage());
            throw new RuntimeException("알고리즘 가이드 수정 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    public boolean deleteAlgorithmGuide(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("유효하지 않은 가이드 ID입니다.");
        }

        try {
            int result = algorithmGuideMapper.deleteAlgorithmGuide(id);
            if (result != 1) {
                throw new RuntimeException("알고리즘 가이드 삭제에 실패했습니다.");
            }
            return true;
        } catch (Exception e) {
            log.error("알고리즘 가이드 삭제 실패 - ID: {}, 오류: {}", id, e.getMessage());
            throw new RuntimeException("알고리즘 가이드 삭제 중 오류가 발생했습니다.", e);
        }
    }

    private void validateGuide(AlgorithmGuide algorithmGuide) {
        if (algorithmGuide == null) {
            throw new IllegalArgumentException("가이드 정보가 없습니다.");
        }
        if (algorithmGuide.getTitle() == null || algorithmGuide.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("제목을 입력해주세요.");
        }
        if (algorithmGuide.getContent() == null || algorithmGuide.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("내용을 입력해주세요.");
        }
    }

}
