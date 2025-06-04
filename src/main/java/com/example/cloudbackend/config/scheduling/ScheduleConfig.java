package com.example.cloudbackend.config.scheduling;

import com.example.cloudbackend.logging.aws.cloudwatch.CloudWatchLogger;
import com.example.cloudbackend.logging.aws.s3.S3Uploader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.SimpleAsyncTaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
public class ScheduleConfig {


    private final S3Uploader s3Uploader;

    public ScheduleConfig(S3Uploader s3Uploader) {
        this.s3Uploader = s3Uploader;
    }

    @Scheduled(cron = "0 5 0 * * ?", zone = "Europe/Stockholm")
    public void uploadToS3(){
        s3Uploader.upload();
    }







}
