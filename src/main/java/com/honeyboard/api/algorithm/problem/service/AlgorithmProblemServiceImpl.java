package com.honeyboard.api.algorithm.problem.service;

import com.honeyboard.api.algorithm.problem.mapper.AlgorithmProblemMapper;
import com.honeyboard.api.algorithm.problem.model.AlgorithmProblem;
import com.honeyboard.api.algorithm.tag.model.Tag;
import com.honeyboard.api.common.model.PageInfo;
import com.honeyboard.api.common.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlgorithmProblemServiceImpl implements AlgorithmProblemService {
    private final AlgorithmProblemMapper apm;

    @Override
    public PageResponse<AlgorithmProblem> getAllProblem(int currentPage, int pageSize) {
        int totalElement = apm.countAlgorithmProblem();
        int offset = (currentPage - 1) * pageSize;
        PageInfo pageInfo = new PageInfo(currentPage, pageSize, totalElement);
        List<AlgorithmProblem> list = apm.selectAllAlgorithmProblem(offset, pageSize);
        return new PageResponse<>(list, pageInfo);
    }

    @Override
    public PageResponse<AlgorithmProblem> searchProblem(int currentPage, int pageSize, String searchType, String keyword) {
        int totalElement = apm.countSearchAlgorithmProblem(searchType, keyword);
        int offset = (currentPage - 1) * pageSize;
        PageInfo pageInfo = new PageInfo(currentPage, pageSize, totalElement);
        List<AlgorithmProblem> list = apm.selectSearchAlgorithmProblem(offset, pageSize, searchType, keyword);
        return new PageResponse<>(list, pageInfo);
    }

    @Override
    @Transactional
    public AlgorithmProblem addProblem(AlgorithmProblem algorithmProblem) {
        log.info("알고리즘 문제 생성 시작 - 제목: {}", algorithmProblem.getTitle());
        int createRes = apm.insertAlgorithmProblem(algorithmProblem);
        if (createRes != 1) {
            log.error("문제 생성 실패 - 제목: {}", algorithmProblem.getTitle());
            throw new RuntimeException("문제 생성에 실패했습니다.");
        }

        if (algorithmProblem.getTags() != null && !algorithmProblem.getTags().isEmpty()) {
            updateProblemTags(algorithmProblem.getId(), algorithmProblem.getTags());
        }

        log.info("알고리즘 문제 생성 완료 - ID: {}", algorithmProblem.getId());
        return algorithmProblem;
    }

    @Override
    @Transactional
    public AlgorithmProblem updateProblem(int problemId, AlgorithmProblem algorithmProblem) {
        log.info("알고리즘 문제 수정 시작 - ID: {}", problemId);
        int result = apm.updateAlgorithmProblem(problemId, algorithmProblem);
        if (result != 1) {
            log.error("문제 수정 실패 - ID: {}", problemId);
            throw new RuntimeException("문제 수정에 실패했습니다.");
        }

        updateProblemTags(problemId, algorithmProblem.getTags());

        log.info("알고리즘 문제 수정 완료 - ID: {}", problemId);
        return algorithmProblem;
    }

    @Override
    public int softDeleteProblem(int problemId) {
        return apm.deleteAlgorithmProblem(problemId);
    }

    @Override
    public boolean existsByUrl(String url) {
        return apm.existsByUrl(url) == 1;
    }

    @Override
    public boolean existsById(int id) {
        return apm.existsById(id) == 1;
    }

    @Transactional
    protected void updateProblemTags(int problemId, List<Tag> tags) {
        if (tags == null) return;

        log.debug("문제 태그 업데이트 시작 - 문제 ID: {}", problemId);
        List<Integer> existingTagIds = apm.getTagIdsByProblemId(problemId);
        List<Integer> newTagIds = tags.stream()
                .map(Tag::getId)
                .toList();

        List<Integer> tagsToRemove = existingTagIds.stream()
                .filter(id -> !newTagIds.contains(id))
                .collect(Collectors.toList());

        List<Integer> tagsToAdd = newTagIds.stream()
                .filter(id -> !existingTagIds.contains(id))
                .collect(Collectors.toList());

        if (!tagsToRemove.isEmpty()) {
            log.debug("태그 삭제 - 문제 ID: {}, 삭제할 태그: {}", problemId, tagsToRemove);
            apm.deleteSpecificAlgorithmProblemTags(problemId, tagsToRemove);
        }

        if (!tagsToAdd.isEmpty()) {
            log.debug("태그 추가 - 문제 ID: {}, 추가할 태그: {}", problemId, tagsToAdd);
            apm.insertAlgorithmProblemTags(problemId, tagsToAdd);
        }
    }
}
