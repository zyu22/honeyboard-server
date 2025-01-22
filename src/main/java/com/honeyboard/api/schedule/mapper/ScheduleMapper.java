package com.honeyboard.api.schedule.mapper;

import com.honeyboard.api.schedule.model.request.SceduleRequest;
import com.honeyboard.api.schedule.model.response.ScheduleList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScheduleMapper {

	// 일정 추가
	int insertSchedule(@Param("schedule") SceduleRequest schedule,
					   @Param("userId") int userId,
					   @Param("generationId") int generationId);

	// 일정 조회
	List<ScheduleList> selectScheduleByMonth(@Param("year") int year,
											 @Param("month") int month,
											 @Param("generationId") Integer generationId,
											 @Param("role") String role);

	// 트랙 일정 조회
	List<ScheduleList> selectTrackProjectByMonth(@Param("year") int year,
													 @Param("month") int month,
													 @Param("generationId") Integer generationId);
	// 일정 수정
	int updateSchedule(@Param("schedule") SceduleRequest schedule,
					   @Param("id") int id);
	// 일정 삭제
	int deleteSchedule(int id);
}
