package com.example.cloudbackend.config.scheduling;

import com.example.cloudbackend.logging.aws.cloudwatch.CloudWatchLogger;
import com.example.cloudbackend.logging.aws.s3.S3Uploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ScheduleConfig {

    private final CloudWatchLogger cloudWatchLogger;

    private final S3Uploader s3Uploader;

    public ScheduleConfig(CloudWatchLogger cloudWatchLogger, S3Uploader s3Uploader) {
        this.cloudWatchLogger = cloudWatchLogger;
        this.s3Uploader = s3Uploader;
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void uploadToS3(){
        s3Uploader.upload();
    }

    @Scheduled(fixedDelay = 3000)
    public void sendToCloudwatch(){
        cloudWatchLogger.upload();
    }


}
