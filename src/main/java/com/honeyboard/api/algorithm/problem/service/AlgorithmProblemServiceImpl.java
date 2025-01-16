package com.honeyboard.api.algorithm.problem.service;

import com.honeyboard.api.algorithm.problem.mapper.AlgorithmProblemMapper;
import com.honeyboard.api.algorithm.problem.model.request.AlgorithmProblemRequest;
import com.honeyboard.api.algorithm.problem.model.response.AlgorithmProblemDetail;
import com.honeyboard.api.algorithm.problem.model.response.AlgorithmProblemList;
import com.honeyboard.api.algorithm.tag.mapper.TagMapper;
import com.honeyboard.api.algorithm.tag.model.response.TagResponse;
import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.common.model.PageInfo;
import com.honeyboard.api.common.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlgorithmProblemServiceImpl implements AlgorithmProblemService {
    private final AlgorithmProblemMapper apm;
    private final TagMapper tm;

    // AlgorithmProblem 전체조회
    @Override
    public PageResponse<AlgorithmProblemList> getAllProblem(int currentPage, int pageSize, String searchType, String keyword) {
        log.info("알고리즘 문제 전체 조회 시작 - 현재 페이지: {}, 검색 조건: {}", currentPage, searchType);
        if (currentPage <= 0 || pageSize <= 0) {
            throw new IllegalArgumentException("페이지 번호와 크기는 1 이상이어야 합니다.");
        }

        int totalElement = apm.countAlgorithmProblem(searchType, keyword);
        int offset = (currentPage - 1) * pageSize;

        if (totalElement > 0 && offset >= totalElement) {
            throw new IllegalArgumentException("요청하신 페이지가 전체 페이지 범위를 초과했습니다.");
        }

        PageInfo pageInfo = new PageInfo(currentPage, pageSize, totalElement);
        List<AlgorithmProblemList> list = apm.selectAllAlgorithmProblem(offset, pageSize, searchType, keyword);

        log.info("알고리즘 문제 전체 조회 완료 - 전체 문제 수: {}", totalElement);

        return new PageResponse<>(list, pageInfo);
    }

    // AlgorithmProblem 상세조회
    @Override
    public  AlgorithmProblemDetail getProblem(int problemId) {
        if (problemId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 알고리즘 문제 ID입니다.");
        }

        log.info("알고리즘 문제 상세 조회 시작 - 문제ID: {}", problemId);

        // Problem 기본 정보 조회
        AlgorithmProblemDetail problemDetail = apm.selectAlgorithmProblem(problemId);
        if (problemDetail == null) {
            throw new IllegalArgumentException("해당 알고리즘 문제를 찾을 수 없습니다.");
        }

        // Tag list 조회
        List<TagResponse> tags = tm.selectProblemTags(problemId);
        problemDetail.setTags(tags);

        log.info("알고리즘 문제 상세 조회 완료 - 문제ID: {}, 태그 수: {}", problemId, tags.size());

        return problemDetail;
    }

    // AlgorithmProblem 문제 작성
    @Override
    @Transactional
    public CreateResponse addProblem(AlgorithmProblemRequest request, int userId) {
        log.info("알고리즘 문제 생성 시작 - 제목: {}", request.getTitle());

        // 문제 중복 체크 - url
        if (apm.existsByUrl(request) == 1) {
            throw new IllegalArgumentException("이미 존재하는 문제입니다.");
        }

        // 문제 생성
        CreateResponse createResponse = new CreateResponse();
        int result = apm.insertAlgorithmProblem(request, userId, createResponse);

        if (result != 1) {
            log.error("문제 생성 실패 - 제목: {}", request.getTitle());
            throw new IllegalArgumentException("생성된 알고리즘 문제 ID를 가져오는데 실패했습니다.");
        }

        // 태그 정보 저장
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            updateProblemTags(createResponse.getId(), request.getTags());
        }

        log.info("알고리즘 문제 생성 완료 - ID: {}", createResponse.getId());
        return createResponse;
    }

    // AlgorithmProblem 문제 수정
    @Override
    public void updateProblem(int problemId, AlgorithmProblemRequest request) {
        log.info("알고리즘 문제 수정 시작 - ID: {}", problemId);

        if (problemId <= 0) {
            log.error("잘못된 문제 ID - ID: {}", problemId);
            throw new IllegalArgumentException("유효하지 않은 문제 ID입니다.");
        }

        int updateResult = apm.updateAlgorithmProblem(request, problemId);

        if (updateResult != 1) {
            log.error("문제 수정 실패 - ID: {}", problemId);
            throw new RuntimeException("문제 수정에 실패했습니다.");
        }

        // 태그 정보 업데이트
        updateProblemTags(problemId, request.getTags());

        log.info("알고리즘 문제 수정 완료 - ID: {}", problemId);
    }

    // AlgorithmProblem 문제 삭제
    @Override
    public void softDeleteProblem(int problemId) {
        log.info("알고리즘 문제 삭제 시작 - ID: {}", problemId);

        if (problemId <= 0) {
            log.error("잘못된 문제 ID - ID: {}", problemId);
            throw new IllegalArgumentException("유효하지 않은 문제 ID입니다.");
        }

        int res = apm.deleteAlgorithmProblem(problemId);

        if (res != 1) {
            log.error("문제 삭제 실패 - ID: {}", problemId);
            throw new RuntimeException("문제 삭제에 실패했습니다.");
        }

        log.info("알고리즘 문제 삭제 완료 - ID: {}", problemId);
    }

    @Transactional
    protected void updateProblemTags(int problemId, List<TagResponse> newTags) {
        if (problemId <= 0) {
            log.error("잘못된 문제 ID - ID: {}", problemId);
            throw new IllegalArgumentException("유효하지 않은 문제 ID입니다.");
        }

        if (newTags == null) return;

        //새로운 태그 처리
        List<Integer> newTagIds = new ArrayList<>();
        for (TagResponse tag : newTags) {
            if (tag.getId() > 0) {
                //기존 태그
                newTagIds.add(tag.getId());
            }
            else{
                //새로운 태그 추가
                TagResponse existingTag = tm.selectTagByName(tag.getName());
                if (existingTag != null) {
                    newTagIds.add(existingTag.getId());
                }
                else {
                    TagResponse newTag = new TagResponse();
                    newTag.setName(tag.getName());
                    tm.insertTag(newTag);
                    newTagIds.add(newTag.getId());
                }
            }
        }

        List<Integer> existingTagIds = tm.getTagIdsByProblemId(problemId);

        List<Integer> tagsToRemove = existingTagIds.stream()
                .filter(id -> !newTagIds.contains(id))
                .collect(Collectors.toList());

        List<Integer> tagsToAdd = newTagIds.stream()
                .filter(id -> !existingTagIds.contains(id))
                .collect(Collectors.toList());

        try {
            if (!tagsToRemove.isEmpty()) {
                log.debug("태그 삭제 - 문제 ID: {}, 삭제할 태그: {}", problemId, tagsToRemove);
                tm.deleteSpecificAlgorithmProblemTags(problemId, tagsToRemove);
            }

            if (!tagsToAdd.isEmpty()) {
                log.debug("태그 추가 - 문제 ID: {}, 추가할 태그: {}", problemId, tagsToAdd);
                tm.insertAlgorithmProblemTags(problemId, tagsToAdd);
            }
        } catch (Exception e) {
            log.error("태그 업데이트 실패 - 문제 ID: {}", problemId, e);
            throw new RuntimeException("태그 업데이트에 실패했습니다.");
        }
    }
}
