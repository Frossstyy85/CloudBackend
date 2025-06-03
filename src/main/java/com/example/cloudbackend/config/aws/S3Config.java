package com.example.cloudbackend.config.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@EnableScheduling
public class S3Config {

    private final String ACCESS_KEY;
    private final String SECRET_KEY;

    public S3Config(@Value("${aws.access.key}") String accessKey,
                    @Value("${aws.secret.key}") String secretKey) {
        ACCESS_KEY = accessKey;
        SECRET_KEY = secretKey;
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.EU_NORTH_1)
                .credentialsProvider(() -> AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY))
                .build();
    }





}
