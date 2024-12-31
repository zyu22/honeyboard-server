package com.honeyboard.api.schedule.service;

import com.honeyboard.api.schedule.model.Schedule;

import java.util.List;

public interface ScheduleService {

	void addSchedule(Schedule schedule);
	List<Schedule> getScheduleByMonth(int year, int month, Integer generationId, String role);
	void updateSchedule(Schedule schedule);
	void deleteSchedule(int scheduleId);
}
