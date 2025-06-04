package com.example.cloudbackend.logging.cloudwatch;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.*;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("prod")
public class CloudWatchLogger  {

    @Value("${aws.cloudwatch.log.group}")
    private String logGroup;
    @Value("${aws.cloudwatch.log.stream}")
    private String logStream;


    private final CloudWatchLogsClient cloudWatchClient;
    private String sequenceToken;

    public CloudWatchLogger(CloudWatchLogsClient cloudWatchClient){

        this.cloudWatchClient = cloudWatchClient;
    }

    @PostConstruct
    public void init(){
        sequenceToken = getSequenceToken();
    }


    /**
     * runs every 20 seconds, and it looks for logs to send to cloudwatch, returns early if there are none
     */
    public void upload(){

        if (CloudWatchBuffer.hasEntries()) {

            List<InputLogEvent> batch = new ArrayList<>();
            while (CloudWatchBuffer.hasEntries()) {
                batch.add(CloudWatchBuffer.poll());
            }

            PutLogEventsRequest request = PutLogEventsRequest.builder()
                    .logEvents(batch)
                    .logStreamName(logStream)
                    .logGroupName(logGroup)
                    .sequenceToken(sequenceToken)
                    .build();

            PutLogEventsResponse response = cloudWatchClient.putLogEvents(request);
            sequenceToken = response.nextSequenceToken();
        }
    }

    private String getSequenceToken(){
        DescribeLogStreamsRequest request = DescribeLogStreamsRequest.builder()
                .logGroupName(logGroup)
                .logStreamNamePrefix(logStream)
                .build();

        DescribeLogStreamsResponse response = cloudWatchClient.describeLogStreams(request);
        List<LogStream> streams = response.logStreams();
        return streams.getFirst().uploadSequenceToken();
    }




}