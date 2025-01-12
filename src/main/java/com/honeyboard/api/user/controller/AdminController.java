package com.honeyboard.api.user.controller;

import com.honeyboard.api.generation.model.GenerationList;
import com.honeyboard.api.user.model.UserInfo;
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
    public ResponseEntity<?> getUsersByGeneration(@RequestParam(value = "generationId", required = false) Integer generationId) {
        log.info("기수별 학생 정보 조회 요청 - 기수: {}", generationId);
        List<UserInfo> users = adminService.getUserByGeneration(generationId);

        if (users.isEmpty() || users.size() == 0) {
            log.info("기수별 학생 조회 완료, 값 없음");
            return ResponseEntity.noContent().build();
        }

        log.info("기수별 학생 조회 완료 - 기수: {}", generationId);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserByUserId(@PathVariable("userId") int userId) {
        log.info("유저 상세정보 조회 요청 - 유저 ID: {}", userId);
        UserInfo userInfo = adminService.getUserByUserId(userId);
        log.info("유저 상세정보 조회 완료 - 유저 ID: {}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(userInfo);
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable("userId") int userId,
                                        @RequestBody UserInfo userInfo) {
        log.info("유저 정보 수정 요청 - 유저 ID: {}", userId);
        adminService.updateUser(userId, userInfo);
        log.info("유저 정보 수정 완료 - 유저 ID: {}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(userInfo);
    }

    @PostMapping("/generation")
    public ResponseEntity<?> createGeneration(@RequestBody GenerationList generation) {  // 제네릭 타입 추가
        log.info("기수 등록 요청 - 기수 번호 : {}", generation.getName());
        int generationId = adminService.addGeneration(generation);  // ID 받기
        log.info("기수 등록 완료 - 기수 번호 : {}, ID: {}", generation.getName(), generationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(generationId);  // ID를 응답 본문에 포함
    }

    @PatchMapping("/generation/{generationId}/isActive")
    public ResponseEntity<?> updateGenerationIsActive(@PathVariable("generationId") int generationId) {
        log.info("기수 활성화 상태 수정 요청 - 기수 ID: {}", generationId);
        adminService.updateGenerationIsActive(generationId);
        log.info("기수 활성화 상태 수정 완료 - 기수 ID: {}", generationId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/generation/{generationId}")
    public ResponseEntity<?> deleteGeneration(@PathVariable("generationId") int generationId) {
        log.info("기수 삭제 요청 - 기수 ID: {}", generationId);
        adminService.removeGeneration(generationId);
        log.info("기수 삭제 완료 - 기수 ID: {}", generationId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
