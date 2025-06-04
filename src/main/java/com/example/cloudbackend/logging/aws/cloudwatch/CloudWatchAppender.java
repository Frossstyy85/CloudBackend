package com.example.cloudbackend.logging.aws.cloudwatch;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


@Component
@Profile("prod")
public class CloudWatchAppender extends AppenderBase<ILoggingEvent>  {

    private final CloudWatchLogger cloudWatchLogger;

    public CloudWatchAppender(CloudWatchLogger cloudWatchLogger) {
        this.cloudWatchLogger = cloudWatchLogger;
        System.out.println("Creating cloudwatch appender bean");
    }

    @Override
    protected void append(ILoggingEvent event) {
        cloudWatchLogger.append(event.getFormattedMessage(), event.getTimeStamp());
    }


    @Override
    public void start() {
        super.start();
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        AppenderDelegator<ILoggingEvent> delegate = (AppenderDelegator<ILoggingEvent>) rootLogger.getAppender("CLOUDWATCH");
        delegate.setDelegateAndReplayBuffer(this);
    }

}

