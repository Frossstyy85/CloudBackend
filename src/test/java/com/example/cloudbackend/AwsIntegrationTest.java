package com.example.cloudbackend;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.*;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class AwsIntegrationTest {

    @Autowired
    private S3Client s3Client;

    @Value("${aws.bucket.name}")
    private String bucketName;


    @Test
    public void s3BucketTest() {
        assertThat(s3Client).isNotNull();

        ListBucketsResponse response = s3Client.listBuckets();
        List<Bucket> buckets = response.buckets();
        assertThat(buckets)
                .extracting(Bucket::name)
                .contains(bucketName);
    }

}
