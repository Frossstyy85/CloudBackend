package com.example.cloudbackend.logging.aws.cloudwatch;

import ch.qos.logback.core.Appender;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AppenderDelegator<E> extends UnsynchronizedAppenderBase<E> {

    private final Queue<E> logBuffer = new ConcurrentLinkedQueue<>();
    private Appender<E> delegate;

    @Override
    protected void append(E event) {
        if (delegate != null) {
            delegate.doAppend(event);
        } else {
            logBuffer.offer(event);
        }
    }

    public void setDelegateAndReplayBuffer(Appender<E> delegate) {
        this.delegate = delegate;
        E event;
        while ((event = logBuffer.poll()) != null) {
            delegate.doAppend(event);
        }
    }


}
