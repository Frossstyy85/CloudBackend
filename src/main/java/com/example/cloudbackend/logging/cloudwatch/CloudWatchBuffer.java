package com.example.cloudbackend.logging.cloudwatch;

import software.amazon.awssdk.services.cloudwatchlogs.model.InputLogEvent;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CloudWatchBuffer {

    private static final ConcurrentLinkedQueue<InputLogEvent> queue = new ConcurrentLinkedQueue<>();

    public static void push(String message, long timestamp) {
        queue.add(InputLogEvent.builder()
                .message(message)
                .timestamp(timestamp)
                .build());
    }

    public static InputLogEvent poll() {
        return queue.poll();
    }

    public static boolean hasEntries() {
        return !queue.isEmpty();
    }


}
