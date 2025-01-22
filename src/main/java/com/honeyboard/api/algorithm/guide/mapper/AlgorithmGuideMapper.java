package com.honeyboard.api.algorithm.guide.mapper;

import com.honeyboard.api.algorithm.guide.model.request.AlgorithmGuideRequest;
import com.honeyboard.api.algorithm.guide.model.response.AlgorithmGuideDetail;
import com.honeyboard.api.algorithm.guide.model.response.AlgorithmGuideList;
import com.honeyboard.api.common.model.CreateResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlgorithmGuideMapper {

    List<AlgorithmGuideList> getAlgorithmGuides(@Param("offset") int offset, @Param("pageSize") int pageSize,
                                                @Param("generationId") int generationId);

    List<AlgorithmGuideList> searchAlgorithmGuide(@Param("offset") int offset, @Param("pageSize") int pageSize,
                                              @Param("generationId") int generationId, @Param("title") String title);

    AlgorithmGuideDetail getAlgorithmGuideDetail (@Param("id") int id, @Param("userId") int userId);


    int addAlgorithmGuide (@Param("algorithmGuideRequest") AlgorithmGuideRequest algorithmGuideRequest, @Param("userId") int userId,
                           @Param("createResponse") CreateResponse response);

    int updateAlgorithmGuide(@Param("id") int id, @Param("algorithmGuideRequest") AlgorithmGuideRequest algorithmGuideRequest, @Param("userId") int userId);

    int softDeleteAlgorithmGuide(int id);

    int countAlgorithmGuide(int generationId);

    int countSearchAlgorithmGuide(@Param("generationId") int generationId, @Param("title") String title);
}
