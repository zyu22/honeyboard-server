package com.honeyboard.api.algorithm.tag.mapper;

import com.honeyboard.api.algorithm.tag.model.response.TagResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagMapper {
    List<TagResponse> selectAllTag();

    List<TagResponse> selectSearchTag(@Param("name") String input);

    int insertTag(TagResponse tag);
}
