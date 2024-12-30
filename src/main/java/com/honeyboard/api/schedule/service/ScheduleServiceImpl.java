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
	public void addSchedule(Schedule schedule) {
		log.info("일정 추가 시작 - 내용: {}", schedule.getContent());

		if (schedule == null) {
			throw new IllegalArgumentException("일정 정보가 없습니다.");
		}

		int result = scheduleMapper.insertSchedule(schedule);

		if (result != 1) {
			throw new IllegalArgumentException("일정 추가에 실패했습니다.");
		}

		log.info("일정 추가 완료 - 일정 ID: {}", schedule.getScheduleId());
	}

	@Override
	public List<Schedule> getScheduleByMonth(int year, int month, Integer generationId, String role) {
		log.info("월별 일정 조회 시작 - year: {}, month: {}, generationId: {}, role: {}",
				year, month, generationId, role);

		if (year <= 0 || month <= 0 || month > 12) {
			throw new IllegalArgumentException("유효하지 않은 날짜입니다.");
		}

		List<Schedule> schedules = scheduleMapper.selectScheduleByMonth(year, month, generationId, role);
		List<TrackProject> trackProjects = scheduleMapper.selectTrackProjectByMonth(year, month, generationId);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		for (TrackProject project : trackProjects) {
			try {
				Schedule schedule = new Schedule();
				schedule.setScheduleId(project.getTrackProjectId());
				schedule.setContent(project.getTitle());

				java.util.Date utilDate = formatter.parse(project.getCreatedAt());
				java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
				schedule.setStartDate(sqlDate);
				schedule.setEndDate(sqlDate);

				schedule.setScheduleType("TRACK");
				schedule.setPublicAccess(true);
				schedule.setGenerationId(project.getGenerationId());
				schedule.setUserId(project.getUserId());

				schedules.add(schedule);
			} catch (ParseException e) {
				log.error("날짜 변환 실패 - 프로젝트 ID: {}", project.getTrackProjectId(), e);
				throw new IllegalArgumentException("날짜 형식이 올바르지 않습니다.");
			}
		}

		Collections.sort(schedules, (s1, s2) -> s1.getStartDate().compareTo(s2.getStartDate()));

		log.info("월별 일정 조회 완료 - 조회된 일정 수: {}", schedules.size());
		return schedules;
	}

	@Override
	public void updateSchedule(Schedule schedule) {
		log.info("일정 수정 시작 - 일정 ID: {}", schedule.getScheduleId());

		if (schedule == null) {
			throw new IllegalArgumentException("일정 정보가 없습니다.");
		}

		int result = scheduleMapper.updateSchedule(schedule);

		if (result != 1) {
			throw new IllegalArgumentException("일정 수정에 실패했습니다.");
		}

		log.info("일정 수정 완료 - 일정 ID: {}", schedule.getScheduleId());
	}

	@Override
	public void deleteSchedule(int scheduleId) {
		log.info("일정 삭제 시작 - 일정 ID: {}", scheduleId);

		if (scheduleId <= 0) {
			throw new IllegalArgumentException("유효하지 않은 일정 ID입니다.");
		}

		int result = scheduleMapper.deleteSchedule(scheduleId);

		if (result != 1) {
			throw new IllegalArgumentException("일정 삭제에 실패했습니다.");
		}

		log.info("일정 삭제 완료 - 일정 ID: {}", scheduleId);
	}

}
