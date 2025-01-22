package com.honeyboard.api.algorithm.language.mapper;

import com.honeyboard.api.algorithm.language.model.LanguageList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProgrammingLanguageMapper {

	List<LanguageList> selectAllProgrammingLanguage();
	
}
