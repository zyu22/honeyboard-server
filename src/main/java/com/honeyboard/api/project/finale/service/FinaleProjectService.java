package com.honeyboard.api.project.finale.service;

import java.util.List;

import com.honeyboard.api.project.finale.model.FinaleProject;

public interface FinaleProjectService {

	List<FinaleProject> getAllFinaleProject(int generationId);

	boolean saveFinaleProject(FinaleProject finaleProject);

	boolean updateFinaleProject(FinaleProject finaleProject);

	boolean softDeleteFinaleProject(int teamId);

}
