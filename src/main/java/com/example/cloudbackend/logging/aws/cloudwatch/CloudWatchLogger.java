package com.example.cloudbackend.logging.aws.cloudwatch;

import org.springframework.scheduling.annotation.Scheduled;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Runs before Spring context is ready, So cannot use Spring Components
 */
public class CloudWatchLogger {

    private final int MAX_BATCH_SIZE = 1_048_576;
    private final int MAX_EVENTS = 10_000;
    Queue<InputLogEvent> eventQueue = new ConcurrentLinkedQueue<>();
    private CloudWatchLogsClient cloudWatchClient;

    private void createClient(){
        this.cloudWatchClient = CloudWatchLogsClient.builder()
                .region(Region.of(System.getenv("AWS_MAIN_REGION")))
                .build();
    }
    private void closeClient(){
            cloudWatchClient.close();
    }

    public CloudWatchLogger(){
        groupName = System.getenv("CLOUDWATCH_LOG_GROUP");
        streamName = System.getenv("CLOUDWATCH_LOG_STREAM");
        System.out.println(groupName);
        System.out.println(streamName);
        createClient();
        sequenceToken = getSequenceToken();
        startLoggerThread();
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
        return response.logStreams().getFirst().uploadSequenceToken();
    }

    private void startLoggerThread(){
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor((r) -> {
            Thread t = new Thread(r, "CloudWatch-thread");
            t.setDaemon(false);
            return t;
        });
        executor.scheduleAtFixedRate(() -> flush(), 2, 20, TimeUnit.MINUTES);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            flush();
            closeClient();
        }));
    }


    public void log(String message, Long timestamp){
        InputLogEvent logEvent = InputLogEvent.builder()
                .message(message)
                .timestamp(timestamp)
                .build();
        eventQueue.add(logEvent);
    }

    private void flush(){
        if (eventQueue.isEmpty())
            return;
        int batchSize = 0;
        int events = 0;
        List<InputLogEvent> batch = new ArrayList<>();
        while (!eventQueue.isEmpty() && events <= MAX_EVENTS){
            batchSize += eventQueue.peek().message().getBytes(StandardCharsets.UTF_8).length + 28;
            if (batchSize > MAX_BATCH_SIZE)
                break;
            batch.add(eventQueue.poll());
            events++;
        }

        PutLogEventsRequest request = PutLogEventsRequest.builder()
                .logEvents(batch)
                .logStreamName(streamName)
                .logGroupName(groupName)
                .sequenceToken(sequenceToken)
                .build();

        try {
            PutLogEventsResponse response = cloudWatchClient.putLogEvents(request);
            sequenceToken = response.nextSequenceToken();
        } catch (SdkException e) {
            e.printStackTrace();
        }

    }



}
