package com.honeyboard.api.image;

import com.honeyboard.api.common.service.S3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;

@RestController
@RequestMapping("/api/v1/image")
@RequiredArgsConstructor
@Slf4j
public class ImageController {
    private final S3Service s3;

    @PostMapping
    public ResponseEntity<?> upLoadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("파일이 비었습니다.");
            }
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body("이미지 파일만 업로드 가능합니다.");
            }
            String imageURL = s3.uploadFile(file);
            return ResponseEntity.ok().body(Collections.singletonMap("url", imageURL));
        } catch (Exception e) {
            log.error("이미지 업로드 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> removeImage(@RequestParam("imageUrl") String imageUrl) {
        try {
            String fileName = s3.extractFileNameFromUrl(imageUrl);
            s3.deleteFile(fileName);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("이미지 삭제 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
