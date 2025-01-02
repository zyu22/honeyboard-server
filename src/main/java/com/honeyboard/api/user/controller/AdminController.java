package com.honeyboard.api.user.controller;

import com.honeyboard.api.user.model.User;
import com.honeyboard.api.user.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/user")
    public ResponseEntity<?> getUsersByGeneration(@RequestParam("generationId") Integer generationId) {
        log.info("기수별 학생 정보 조회 요청 - 기수: {}", generationId);
        List<User> users = adminService.getUserByGeneration(generationId);

        if(users.isEmpty() || users.size() == 0) {
            log.info("기수별 학생 조회 완료, 값 없음");
            return ResponseEntity.noContent().build();
        }

        log.info("기수별 학생 조회 완료 - 기수: {}", generationId);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }





}
