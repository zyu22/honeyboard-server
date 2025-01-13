package com.honeyboard.api.schedule.mapper;

import com.honeyboard.api.project.track.model.response.TrackProjectList;
import com.honeyboard.api.schedule.model.Schedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScheduleMapper {

	int insertSchedule(Schedule schedule);
	List<Schedule> selectScheduleByMonth(@Param("year") int year,
										 @Param("month") int month,
										 @Param("generationId") Integer generationId,
										 @Param("role") String role);
	List<TrackProjectList> selectTrackProjectByMonth(@Param("year") int year,
													 @Param("month") int month,
													 @Param("generationId") Integer generationId);
	int updateSchedule(Schedule schedule);
	int deleteSchedule(int scheduleId);
}
