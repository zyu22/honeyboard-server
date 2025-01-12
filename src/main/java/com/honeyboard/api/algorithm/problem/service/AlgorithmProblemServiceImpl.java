package com.honeyboard.api.algorithm.problem.service;

import com.honeyboard.api.algorithm.problem.mapper.AlgorithmProblemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlgorithmProblemServiceImpl implements AlgorithmProblemService {
    private final AlgorithmProblemMapper apm;

//    @Override
//    public PageResponse<AlgorithmProblem> getAllProblem(int currentPage, int pageSize) {
//        log.info("알고리즘 문제 전체 조회 시작 - 현재 페이지: {}", currentPage);
//
//        if (currentPage <= 0 || pageSize <= 0) {
//            throw new IllegalArgumentException("페이지 번호와 크기는 1 이상이어야 합니다.");
//        }
//
//        int totalElement = apm.countAlgorithmProblem();
//        int offset = (currentPage - 1) * pageSize;
//
//        if (totalElement > 0 && offset >= totalElement) {
//            throw new IllegalArgumentException("요청하신 페이지가 전체 페이지 범위를 초과했습니다.");
//        }
//
//        PageInfo pageInfo = new PageInfo(currentPage, pageSize, totalElement);
//        List<AlgorithmProblem> list = apm.selectAllAlgorithmProblem(offset, pageSize);
//
//        log.info("알고리즘 문제 전체 조회 완료 - 전체 문제 수: {}", totalElement);
//
//        return new PageResponse<>(list, pageInfo);
//    }
//
//    @Override
//    public PageResponse<AlgorithmProblem> searchProblem(int currentPage, int pageSize, String searchType, String keyword) {
//        log.info("알고리즘 문제 검색 시작 - 현재 페이지: {}, 검색 타입: {}, 키워드: {}", currentPage, searchType, keyword);
//
//        if (currentPage <= 0 || pageSize <= 0) {
//            throw new IllegalArgumentException("페이지 번호와 크기는 1 이상이어야 합니다.");
//        }
//
//        if (searchType == null || searchType.trim().isEmpty()) {
//            throw new NullPointerException("검색 타입은 필수 값입니다.");
//        }
//
//        int totalElement = apm.countSearchAlgorithmProblem(searchType, keyword);
//        int offset = (currentPage - 1) * pageSize;
//
//        if (totalElement > 0 && offset >= totalElement) {
//            throw new IllegalArgumentException("요청하신 페이지가 전체 페이지 범위를 초과했습니다.");
//        }
//
//        PageInfo pageInfo = new PageInfo(currentPage, pageSize, totalElement);
//        List<AlgorithmProblem> list = apm.selectSearchAlgorithmProblem(offset, pageSize, searchType, keyword);
//
//        log.info("알고리즘 문제 검색 완료 - 검색 문제 수: {}", totalElement);
//
//        return new PageResponse<>(list, pageInfo);
//    }
//
//    @Override
//    public AlgorithmProblem getProblem(int problemId) {
//        if (problemId <= 0) {
//            throw new IllegalArgumentException("유효하지 않은 알고리즘 문제 ID입니다.");
//        }
//
//        log.debug("알고리즘 문제 상세 조회 시작 - 문제ID: {}", problemId);
//
//        return apm.selectAlgorithmProblem(problemId);
//    }
//
//    @Override
//    @Transactional
//    public AlgorithmProblem addProblem(AlgorithmProblem algorithmProblem) {
//        log.info("알고리즘 문제 생성 시작 - 제목: {}", algorithmProblem.getTitle());
//
//        if (existsByUrl(algorithmProblem.getUrl())) {
//            throw new IllegalArgumentException("이미 존재하는 문제입니다.");
//        }
//
//        int createRes = apm.insertAlgorithmProblem(algorithmProblem);
//        if (createRes != 1) {
//            log.error("문제 생성 실패 - 제목: {}", algorithmProblem.getTitle());
//            throw new RuntimeException("문제 생성에 실패했습니다.");
//        }
//
//        if (algorithmProblem.getTags() != null && !algorithmProblem.getTags().isEmpty()) {
//            updateProblemTags(algorithmProblem.getId(), algorithmProblem.getTags());
//        }
//
//        log.info("알고리즘 문제 생성 완료 - ID: {}", algorithmProblem.getId());
//        return algorithmProblem;
//    }
//
//    @Override
//    @Transactional
//    public AlgorithmProblem updateProblem(int problemId, AlgorithmProblem algorithmProblem) {
//        log.info("알고리즘 문제 수정 시작 - ID: {}", problemId);
//
//        if (problemId <= 0) {
//            log.error("잘못된 문제 ID - ID: {}", problemId);
//            throw new IllegalArgumentException("유효하지 않은 문제 ID입니다.");
//        }
//
//        int result = apm.updateAlgorithmProblem(problemId, algorithmProblem);
//
//        if (result != 1) {
//            log.error("문제 수정 실패 - ID: {}", problemId);
//            throw new RuntimeException("문제 수정에 실패했습니다.");
//        }
//
//        updateProblemTags(problemId, algorithmProblem.getTags());
//
//        log.info("알고리즘 문제 수정 완료 - ID: {}", problemId);
//        return algorithmProblem;
//    }
//
//    @Override
//    public boolean softDeleteProblem(int problemId) {
//        log.info("알고리즘 문제 삭제 시작 - ID: {}", problemId);
//
//        if (problemId <= 0) {
//            log.error("잘못된 문제 ID - ID: {}", problemId);
//            throw new IllegalArgumentException("유효하지 않은 문제 ID입니다.");
//        }
//
//        int res = apm.deleteAlgorithmProblem(problemId);
//
//        if (res != 1) {
//            log.error("문제 삭제 실패 - ID: {}", problemId);
//            throw new RuntimeException("문제 삭제에 실패했습니다.");
//        }
//
//        log.info("알고리즘 문제 삭제 완료 - ID: {}", problemId);
//        return res == 1;
//    }
//
//    @Override
//    public boolean existsByUrl(String url) {
//        return apm.existsByUrl(url) == 1;
//    }
//
//    @Override
//    public boolean existsById(int id) {
//        return apm.existsById(id) == 1;
//    }
//
//    @Transactional
//    protected void updateProblemTags(int problemId, List<TagResponse> tags) {
//        if (problemId <= 0) {
//            log.error("잘못된 문제 ID - ID: {}", problemId);
//            throw new IllegalArgumentException("유효하지 않은 문제 ID입니다.");
//        }
//        for (TagResponse tag : tags) {
//            if (tag.getId() <= 0) {
//                log.error("잘못된 태그 ID - 태그 ID: {}", tag.getId());
//                throw new IllegalArgumentException("유효하지 않은 태그 ID가 포함되어 있습니다.");
//            }
//        }
//
//        if (tags == null) return;
//
//        log.debug("문제 태그 업데이트 시작 - 문제 ID: {}", problemId);
//        List<Integer> existingTagIds = apm.getTagIdsByProblemId(problemId);
//        List<Integer> newTagIds = tags.stream()
//                .map(TagResponse::getId)
//                .toList();
//
//        List<Integer> tagsToRemove = existingTagIds.stream()
//                .filter(id -> !newTagIds.contains(id))
//                .collect(Collectors.toList());
//
//        List<Integer> tagsToAdd = newTagIds.stream()
//                .filter(id -> !existingTagIds.contains(id))
//                .collect(Collectors.toList());
//
//        try {
//            if (!tagsToRemove.isEmpty()) {
//                log.debug("태그 삭제 - 문제 ID: {}, 삭제할 태그: {}", problemId, tagsToRemove);
//                apm.deleteSpecificAlgorithmProblemTags(problemId, tagsToRemove);
//            }
//
//            if (!tagsToAdd.isEmpty()) {
//                log.debug("태그 추가 - 문제 ID: {}, 추가할 태그: {}", problemId, tagsToAdd);
//                apm.insertAlgorithmProblemTags(problemId, tagsToAdd);
//            }
//        } catch (Exception e) {
//            log.error("태그 업데이트 실패 - 문제 ID: {}", problemId, e);
//            throw new RuntimeException("태그 업데이트에 실패했습니다.");
//        }
//    }
}
