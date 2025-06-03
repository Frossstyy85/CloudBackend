package com.example.cloudbackend.common;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.springframework.stereotype.Component;

@Component
public class CloudWatchAppender extends AppenderBase<ILoggingEvent> {

    @Override
    protected void append(ILoggingEvent event) {
        System.out.println("from custom appender: " + event);
    }



}
