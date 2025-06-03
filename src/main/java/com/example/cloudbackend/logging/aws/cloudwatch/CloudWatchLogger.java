package com.example.cloudbackend.logging.aws.cloudwatch;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.*;

import java.util.List;

@Component
public class CloudWatchLogger {

    private CloudWatchLogsClient cloudWatchClient;

    private void createClient(){
        this.cloudWatchClient = CloudWatchLogsClient.builder()
                .region(Region.of("eu-north-1"))
                .build();
    }
    private void closeClient(){
        if (cloudWatchClient != null)
            cloudWatchClient.close();
    }

    public CloudWatchLogger(){
        groupName = System.getenv("CLOUDWATCH_LOG_GROUP");
        streamName = System.getenv("CLOUDWATCH_LOG_STREAM");
        System.out.println(groupName);
        System.out.println(streamName);
        createClient();
        sequenceToken = getSequenceToken();
    }

    private String sequenceToken;
    private final String groupName;
    private final String streamName;


    private String getSequenceToken() {
        DescribeLogStreamsRequest request = DescribeLogStreamsRequest.builder()
                .logGroupName(groupName)
                .logStreamNamePrefix(streamName)
                .build();

        DescribeLogStreamsResponse response = cloudWatchClient.describeLogStreams(request);
        List<LogStream> logStreams = response.logStreams();

        if (!logStreams.isEmpty()) {
            return logStreams.get(0).uploadSequenceToken();
        } else {
            return null;
        }
    }

    public void log(String message, Long timestamp){
        InputLogEvent logEvent = InputLogEvent.builder()
                .message(message)
                .timestamp(timestamp)
                .build();

        PutLogEventsRequest request = PutLogEventsRequest.builder()
                .logEvents(logEvent)
                .logStreamName(streamName)
                .logGroupName(groupName)
                .sequenceToken(sequenceToken)
                .build();

        PutLogEventsResponse response = cloudWatchClient.putLogEvents(request);
        sequenceToken = response.nextSequenceToken();
    }





}
