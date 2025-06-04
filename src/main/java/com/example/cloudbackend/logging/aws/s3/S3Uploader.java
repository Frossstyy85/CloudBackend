package com.example.cloudbackend.logging.aws.s3;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class S3Uploader {

    @Value("${aws.bucket.name}")
    private  String bucketName;

    private final S3Client s3Client;

    public S3Uploader(S3Client s3Client) {
        this.s3Client = s3Client;
    }


    public void upload(){

        String fileName = "logs/log_" + LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".log";
        log.info(fileName);

        Path filePath = Paths.get(fileName);

        if (!Files.exists(filePath)) {
            log.warn("No log file found {}", fileName);
            return;
        }

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key("log_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".log")
                    .build();

            s3Client.putObject(request, RequestBody.fromFile(filePath));
            log.info("Uploaded {} to S3", fileName);

        } catch (RuntimeException e) {
            log.error("S3 upload failed for {}", fileName, e);
        }



    }



}
