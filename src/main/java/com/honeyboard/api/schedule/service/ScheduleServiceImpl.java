package com.honeyboard.api.schedule.service;

import com.honeyboard.api.project.track.model.TrackProject;
import com.honeyboard.api.schedule.mapper.ScheduleMapper;
import com.honeyboard.api.schedule.model.Schedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

	private final ScheduleMapper scheduleMapper;

	@Override
	public boolean addSchedule(Schedule schedule) {
		int result = scheduleMapper.insertSchedule(schedule);
		return result > 0;
	}

	@Override
	public List<Schedule> getScheduleByMonth(int year, int month, Integer generationId, String role) {
		List<Schedule> schedules = scheduleMapper.selectScheduleByMonth(year, month, generationId, role);
		List<TrackProject> trackProjects = scheduleMapper.selectTrackProjectByMonth(year, month, generationId);

		//날짜 포맷 변경
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		for (TrackProject project : trackProjects) {
			try {
				Schedule schedule = new Schedule();
				schedule.setScheduleId(project.getTrackProjectId());
				schedule.setTitle(project.getTitle());

				java.util.Date utilDate = formatter.parse(project.getCreatedAt());
				java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

				schedule.setScheduleType("TRACK");
				schedule.setPublicAccess(true);
				schedule.setGenerationId(project.getGenerationId());
				schedule.setUserId(project.getUserId());

				schedules.add(schedule);
			} catch (ParseException e) {
				log.error("날짜 변환 중 오류 발생: ", e);
			}
		}
		//날짜순 정렬
		Collections.sort(schedules,
				(s1, s2) -> s1.getStartDate().compareTo(s2.getStartDate()));

		return schedules;
	}

	@Override
	public boolean updateSchedule(Schedule schedule) {
		int result = scheduleMapper.updateSchedule(schedule);
		return result > 0;
	}

	@Override
	public boolean deleteSchedule(int scheduleId) {
		int result = scheduleMapper.deleteSchedule(scheduleId);
		return result > 0;
	}

}
