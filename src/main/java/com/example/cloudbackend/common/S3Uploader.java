//package com.example.cloudbackend.common;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import software.amazon.awssdk.core.sync.RequestBody;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;  
//
//@Component
//public class S3Uploader {
//
//
//    private static final Logger log = LoggerFactory.getLogger(S3Uploader.class);
//    private final String BUCKET_NAME;
//    private final String fileName = "";
//    private final S3Client s3Client;
//
//    public S3Uploader(@Value("${aws.bucket.name}") String bucketName, S3Client s3Client) {
//        BUCKET_NAME = bucketName;
//        this.s3Client = s3Client;
//    }
//
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void upload(){
//        Path filePath = Paths.get("logs" + fileName);
//
//        if (!Files.exists(filePath)) {
//            log.warn("No log file found {}", fileName);
//            return;
//        }
//
//        try {
//            PutObjectRequest request = PutObjectRequest.builder()
//                    .bucket(BUCKET_NAME)
//                    .key("log_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".txt")
//                    .build();
//
//            s3Client.putObject(request, RequestBody.fromFile(filePath));
//            log.info("Uploaded {} to S3", fileName);
//
//        } catch (RuntimeException e) {
//            log.error("S3 upload failed for {}", fileName, e);
//        }
//
//
//
//    }
//
//
//}
//
