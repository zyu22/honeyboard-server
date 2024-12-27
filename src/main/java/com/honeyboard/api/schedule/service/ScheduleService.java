package com.honeyboard.api.schedule.service;

import com.honeyboard.api.schedule.model.Schedule;

import java.util.List;

public interface ScheduleService {

	boolean addSchedule(Schedule schedule);
	List<Schedule> getScheduleByMonth(int year, int month, Integer generationId, String role);
	boolean updateSchedule(Schedule schedule);
	boolean deleteSchedule(int scheduleId);
}
