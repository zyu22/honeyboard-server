package com.honeyboard.api.project.finale.mapper;

import java.util.List;

public
interface FinaleProjectMapper {

    public
    List<FinaleProject> selectFinaleProject(int generationId);

    int insertFinaleProject(FinaleProject finaleProject);

    int insertFinaleProjectThumbnail(String thumbnail);

    int updateFinaleProject(FinaleProject finaleProject);

    boolean updateFinaleProjectDeleteStatus(int finaleProjectId);

}
