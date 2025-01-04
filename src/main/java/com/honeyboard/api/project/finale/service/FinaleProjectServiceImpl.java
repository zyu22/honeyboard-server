package com.honeyboard.api.project.finale.service;

import java.util.List;

import com.honeyboard.api.project.finale.mapper.FinaleProjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.honeyboard.api.project.finale.model.FinaleProject;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinaleProjectServiceImpl implements FinaleProjectService {

	private final FinaleProjectMapper finaleProjectMapper;

	@Override
	public List<FinaleProject> getAllFinaleProject(int generationId) {
		if (generationId <= 0) {
			throw new IllegalArgumentException("유효하지 않은 기수 ID입니다.");
		}
		return finaleProjectMapper.selectFinaleProject(generationId);
	}

	@Override
	public boolean addFinaleProjectThumbnail(String thumbnail) {

		return false;
	}

	@Override
	public boolean saveFinaleProject(FinaleProject finaleProject) {
		validateFinaleProject(finaleProject);
		try {
			return finaleProjectMapper.insertFinaleProject(finaleProject) > 0;
		} catch (Exception e) {
			log.error("최종 프로젝트 등록 실패: {}", e.getMessage());
			throw new RuntimeException("프로젝트 등록에 실패했습니다.", e);
		}
	}

	@Override
	public boolean updateFinaleProject(FinaleProject finaleProject) {
		validateFinaleProject(finaleProject);
		if (finaleProject.getFinaleProjectId() <= 0) {
			throw new IllegalArgumentException("유효하지 않은 프로젝트 ID입니다.");
		}
		try {
			return finaleProjectMapper.updateFinaleProject(finaleProject) > 0;
		} catch (Exception e) {
			log.error("최종 프로젝트 수정 실패 - ID: {}, 오류: {}", finaleProject.getFinaleProjectId(), e.getMessage());
			throw new RuntimeException("프로젝트 수정에 실패했습니다.", e);
		}
	}

	@Override
	public boolean softDeleteFinaleProject(int finaleProjectId) {
		if (finaleProjectId <= 0) {
			throw new IllegalArgumentException("유효하지 않은 프로젝트 ID입니다.");
		}
		try {
			return finaleProjectMapper.updateFinaleProjectDeleteStatus(finaleProjectId);
		} catch (Exception e) {
			log.error("최종 프로젝트 삭제 실패 - ID: {}, 오류: {}", finaleProjectId, e.getMessage());
			throw new RuntimeException("프로젝트 삭제에 실패했습니다.", e);
		}
	}

	private void validateFinaleProject(FinaleProject finaleProject) {
		if (finaleProject == null) {
			throw new IllegalArgumentException("프로젝트 정보가 없습니다.");
		}
		if (finaleProject.getName() == null || finaleProject.getName().trim().isEmpty()) {
			throw new IllegalArgumentException("프로젝트 이름을 입력해주세요.");
		}
	}

}
