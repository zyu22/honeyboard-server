package com.honeyboard.api.common.service.S3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3ServiceImp implements S3Service {

    private final AmazonS3 s3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            String fileName = createFileName((file.getOriginalFilename()));
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            s3.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), metadata));
            return s3.getUrl(bucket, fileName).toString();
        } catch (IOException e) {
            log.error("파일 업로드 실패: {}", e.getMessage());
            throw new RuntimeException("파일 업로드에 실패했습니다.", e);
        }

    }

    @Override
    public String createFileName(String originalFileName) {
        return UUID.randomUUID() + "_" + originalFileName;
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            s3.deleteObject(bucket, fileName);
        } catch (AmazonServiceException e) {
            log.error("파일 삭제 실패: {}", e.getMessage());
            throw new RuntimeException("파일 삭제에 실패했습니다.", e);
        }
    }

    @Override
    public String extractFileNameFromUrl(String fileUrl) {
        try {
            return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        } catch (StringIndexOutOfBoundsException e) {
            log.error("URL에서 파일명 추출 실패: {}", e.getMessage());
            throw new RuntimeException("잘못된 파일 URL입니다.", e);
        }
    }
}
