package com.honeyboard.api.schedule.controller;

import com.honeyboard.api.schedule.model.Schedule;
import com.honeyboard.api.schedule.service.ScheduleService;
import com.honeyboard.api.user.model.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/schedule")
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {
    private final ScheduleService scheduleService;

    //일정 등록
    @PostMapping
    public ResponseEntity<?> createSchedule(@RequestBody Schedule schedule) {
        try {
            boolean result = scheduleService.addSchedule(schedule);

            if(result) {
                return ResponseEntity.status(HttpStatus.CREATED).body("일정이 등록되었습니다.");
            }
            throw new Exception("일정 등록에 실패했습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //일정 조회 year, month
    @GetMapping("/{year}/{month}")
    public ResponseEntity<?> getSchedule(@PathVariable int year,
                                         @PathVariable int month,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            String role = userDetails.getUser().getRole();
            Integer generationId = userDetails.getUser().getGenerationId();

            List<Schedule> schedules = scheduleService.getScheduleByMonth(year, month, generationId, role);

            if (schedules == null || schedules.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok().body(schedules);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //일정 수정
    @PutMapping("/{scheduleId}")
    public ResponseEntity<?> updateSchedule (@PathVariable int scheduleId,
                                             @RequestBody Schedule schedule){
        try {
            schedule.setScheduleId(scheduleId);
            boolean result = scheduleService.updateSchedule(schedule);
            if (result) {
                return ResponseEntity.ok().body("일정이 수정되었습니다.");
            }
            throw new Exception("일정 수정에 실패했습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //일정 삭제
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<?> deleteSchedule(@PathVariable int scheduleId) {
        try {
            boolean result = scheduleService.deleteSchedule(scheduleId);
            if (result) {
                return ResponseEntity.ok().body("일정을 삭제했습니다.");
            }
            throw new Exception("일정 삭제에 실패했습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
