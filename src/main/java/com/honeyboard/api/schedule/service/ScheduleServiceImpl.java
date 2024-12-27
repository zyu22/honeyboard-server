package com.honeyboard.api.schedule.service;

import com.honeyboard.api.schedule.model.Schedule;
import com.honeyboard.api.schedule.mapper.ScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

	private final ScheduleMapper scheduleMapper;

	@Override
	public boolean addSchedule(Schedule schedule) {
		int result = scheduleMapper.insertSchedule(schedule);
		return result > 0;
	}

	@Override
	public List<Schedule> getScheduleByMonth(int year, int month, Integer generationId, String role) {
		return scheduleMapper.selectScheduleByMonth(year, month, generationId, role);
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
