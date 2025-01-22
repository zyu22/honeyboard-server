package com.honeyboard.api.common.service.S3;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    String uploadFile(MultipartFile file);

    String createFileName(String originalFileName);

    void deleteFile(String fileName);

    String extractFileNameFromUrl(String fileUrl);
}
