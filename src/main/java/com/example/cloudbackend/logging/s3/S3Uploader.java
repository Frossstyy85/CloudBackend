package com.example.cloudbackend.logging.s3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
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
@Profile({"prod","test"})
public class S3Uploader {

    @Value("${aws.bucket.name}")
    private  String bucketName;

    private final S3Client s3Client;

    public S3Uploader(S3Client s3Client) {
        this.s3Client = s3Client;
    }
    
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void upload(){

        String filePath = "logs/app-" + LocalDate.now().minusDays(1).format(formatter) + ".log";
        log.info(filePath);

        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            log.warn("No log file found {}", filePath);
            return;
        }

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key("log_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".log")
                    .build();

            s3Client.putObject(request, RequestBody.fromFile(path));
            log.info("Uploaded {} to S3", filePath);

        } catch (RuntimeException e) {
            log.error("S3 upload failed for {}", filePath, e);
        }


    }



}
