package com.example.cloudbackend.config;

import com.example.cloudbackend.logging.s3.S3Uploader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling
@Profile("prod")
public class ScheduleConfig {


    private final S3Uploader s3Uploader;

    public ScheduleConfig(S3Uploader s3Uploader) {
        this.s3Uploader = s3Uploader;
    }

    @Scheduled(cron = "0 5 0 * * ?", zone = "Europe/Stockholm")
    public void uploadToS3(){
        log.info("Uploading file to S3");
        s3Uploader.upload();
    }







}
