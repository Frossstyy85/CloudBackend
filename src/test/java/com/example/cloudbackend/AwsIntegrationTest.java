package com.example.cloudbackend;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
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
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class AwsIntegrationTest {

    @Autowired
    private S3Client s3Client;

    @Autowired
    private CloudWatchLogsClient cloudWatchClient;

    @Value("${aws.cloudwatch.log.group}")
    private String logGroupName;

    @Value("${aws.cloudwatch.log.stream}")
    private String logStreamName;

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Test
    public void testClientConnection(){
        assertThat(s3Client).isNotNull();
        assertThat(cloudWatchClient).isNotNull();
    }

    @Test
    public void s3BucketTest(){
        ListBucketsResponse response = s3Client.listBuckets();
        List<Bucket> buckets = response.buckets();
        assertThat(buckets)
                .extracting(Bucket::name)
                .contains(bucketName);

    }

    @Test
    public void cloudWatchGroupTest(){

        DescribeLogGroupsResponse response = cloudWatchClient.describeLogGroups();

        assertThat(response.logGroups())
                .extracting(LogGroup::logGroupName)
                .contains(logGroupName);
    }

    @Test
    public void cloudWatchStreamTest(){
        DescribeLogStreamsResponse response = cloudWatchClient.describeLogStreams(
                DescribeLogStreamsRequest.builder()
                        .logGroupName(logGroupName)
                        .logStreamNamePrefix(logStreamName)
                        .build()
        );

        assertThat(response.logStreams())
                .extracting(LogStream::logStreamName)
                .contains(logStreamName);
    }






}
