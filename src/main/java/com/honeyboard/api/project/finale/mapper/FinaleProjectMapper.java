package com.honeyboard.api.project.finale.mapper;

import com.honeyboard.api.project.finale.model.FinaleProject;

import java.util.List;

public
interface FinaleProjectMapper {

    public
    List<FinaleProject> selectFinaleProject(int generationId);

    public int insertFinaleProject(FinaleProject finaleProject);

    public int updateFinaleProject(FinaleProject finaleProject);

    public boolean updateFinaleProjectDeleteStatus(int finaleProjectId);

}
