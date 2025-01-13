package com.honeyboard.api.schedule.controller;

import com.honeyboard.api.schedule.model.Schedule;
import com.honeyboard.api.schedule.service.ScheduleService;
import com.honeyboard.api.user.model.CurrentUser;
import com.honeyboard.api.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schedule")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {
    private final ScheduleService scheduleService;
//
//    //일정 등록
//    @PostMapping
//    public ResponseEntity<?> createSchedule(@RequestBody Schedule schedule) {
//        log.info("일정 등록 요청 - 내용: {}", schedule.getContent());
//
//        scheduleService.addSchedule(schedule);
//
//        log.info("일정 등록 완료 - 일정 ID: {}", schedule.getScheduleId());
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }
//
//    //일정 조회 year, month
//    @GetMapping("/{year}/{month}")
//    public ResponseEntity<?> getSchedule(@PathVariable int year,
//                                         @PathVariable int month,
//                                         @CurrentUser User user) {
//        log.info("월별 일정 조회 요청 - year: {}, month: {}", year, month);
//
//        String role = user.getRole();
//        Integer generationId = user.getGenerationId();
//
//        List<Schedule> schedules = scheduleService.getScheduleByMonth(year, month, generationId, role);
//
//        if (schedules == null || schedules.isEmpty()) {
//            log.info("조회된 일정 없음 - year: {}, month: {}", year, month);
//            return ResponseEntity.noContent().build();
//        }
//
//        log.info("월별 일정 조회 완료 - 조회된 일정 수: {}", schedules.size());
//        return ResponseEntity.ok(schedules);
//    }
//
//    //일정 수정
//    @PutMapping("/{scheduleId}")
//    public ResponseEntity<?> updateSchedule (@PathVariable int scheduleId,
//                                             @RequestBody Schedule schedule){
//        log.info("일정 수정 요청 - 일정 ID: {}", scheduleId);
//
//        schedule.setScheduleId(scheduleId);
//        scheduleService.updateSchedule(schedule);
//
//        log.info("일정 수정 완료 - 일정 ID: {}", scheduleId);
//        return ResponseEntity.ok().build();
//    }
//
//    //일정 삭제
//    @DeleteMapping("/{scheduleId}")
//    public ResponseEntity<?> deleteSchedule(@PathVariable int scheduleId) {
//        log.info("일정 삭제 요청 - 일정 ID: {}", scheduleId);
//
//        scheduleService.deleteSchedule(scheduleId);
//
//        log.info("일정 삭제 완료 - 일정 ID: {}", scheduleId);
//        return ResponseEntity.ok().build();
//    }
}
