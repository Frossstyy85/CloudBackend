package com.example.cloudbackend.logging.aws.cloudwatch;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class CloudWatchAppender extends AppenderBase<ILoggingEvent> {

    private CloudWatchLogger logger;

    public CloudWatchAppender(){
    }

    @Override
    protected void append(ILoggingEvent event) {
        logger.log(event.getMessage(), event.getTimeStamp());
    }

    @Override
    public void start() {
        logger = new CloudWatchLogger();
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }






}
