package com.example.cloudbackend.logging.aws.cloudwatch;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
@Profile("prod")
public class CloudWatchLogger  {

    @Value("${aws.cloudwatch.log.group}")
    private String logGroup;
    @Value("${aws.cloudwatch.log.stream}")
    private String logStream;

    private final BlockingQueue<InputLogEvent> eventQueue = new LinkedBlockingQueue<>();

    private final CloudWatchLogsClient cloudWatchClient;
    private String sequenceToken;

    public CloudWatchLogger(CloudWatchLogsClient cloudWatchClient){

        this.cloudWatchClient = cloudWatchClient;
    }

    @PostConstruct
    public void init(){
        sequenceToken = getSequenceToken();
    }


    public void append(String message, Long timestamp){
        InputLogEvent event = InputLogEvent.builder()
                .message(message)
                .timestamp(timestamp)
                .build();
        eventQueue.add(event);
    }


    /**
     * runs every 20 seconds, and it looks for logs to send to cloudwatch, returns early if there are none
     */
    @Scheduled(initialDelay = 20, fixedRate = 10,timeUnit = TimeUnit.SECONDS)
    public void upload(){

        if (eventQueue.isEmpty())
            return;

        List<InputLogEvent> batch = new ArrayList<>();
        eventQueue.drainTo(batch);

        PutLogEventsRequest request = PutLogEventsRequest.builder()
                .logEvents(batch)
                .logStreamName(logStream)
                .logGroupName(logGroup)
                .sequenceToken(sequenceToken)
                .build();

        PutLogEventsResponse response = cloudWatchClient.putLogEvents(request);
        sequenceToken = response.nextSequenceToken();
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