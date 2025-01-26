package com.honeyboard.api.schedule.service;

import com.honeyboard.api.common.model.CreateResponse;
import com.honeyboard.api.schedule.model.request.SceduleRequest;
import com.honeyboard.api.schedule.model.response.ScheduleList;

import java.util.List;

public interface ScheduleService {

    // 일정 추가
    CreateResponse addSchedule(SceduleRequest schedule, int userId);
    // 일정 조회
	List<ScheduleList> getScheduleByMonth(int year, int month, Integer generationId, String role);
	// 일정 수정
    void updateSchedule(SceduleRequest schedule, int id);
    // 일정 삭제
	void deleteSchedule(int id);
}
