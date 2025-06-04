package com.example.cloudbackend.config;

import com.example.cloudbackend.logging.cloudwatch.CloudWatchLogger;
import com.example.cloudbackend.logging.s3.S3Uploader;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
@Profile("prod")
public class ScheduleConfig {


    private final S3Uploader s3Uploader;
    private final CloudWatchLogger cloudWatchLogger;

    public ScheduleConfig(S3Uploader s3Uploader, CloudWatchLogger cloudWatchLogger) {
        this.s3Uploader = s3Uploader;
        this.cloudWatchLogger = cloudWatchLogger;
    }

    @Scheduled(cron = "0 5 0 * * ?", zone = "Europe/Stockholm")
    public void uploadToS3(){
        s3Uploader.upload();
    }

    @Scheduled(initialDelay = 20, fixedRate = 10,timeUnit = TimeUnit.SECONDS)
    public void uploadToCloudWatch(){
        cloudWatchLogger.upload();
    }







}
