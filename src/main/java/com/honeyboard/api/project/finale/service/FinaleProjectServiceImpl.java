package com.honeyboard.api.project.finale.service;

import java.util.List;

import com.honeyboard.api.project.finale.mapper.FinaleProjectMapper;
import org.springframework.stereotype.Service;

import com.honeyboard.api.project.finale.model.FinaleProject;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FinaleProjectServiceImpl implements FinaleProjectService {

	private final FinaleProjectMapper finaleProjectMapper;


	@Override
	public
	List<FinaleProject> getAllFinaleProject(int generationId) {
		return finaleProjectMapper.selectFinaleProject(generationId);
	}

	@Override
	public boolean saveFinaleProject(FinaleProject finaleProject) {
		return finaleProjectMapper.insertFinaleProject(finaleProject) > 0;
	}

	@Override
	public boolean updateFinaleProject(FinaleProject finaleProject) {
		return finaleProjectMapper.updateFinaleProject(finaleProject) > 0;
	}

	@Override
	public boolean softDeleteFinaleProject(int finaleProjectId) {
		return finaleProjectMapper.updateFinaleProjectDeleteStatus(finaleProjectId);
	}

}
