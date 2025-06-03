package com.example.cloudbackend.logging.aws.s3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class LoginLogger {

    private static final Logger loginLogger = LoggerFactory.getLogger("login-logger");

    @EventListener
    public void log(AuthenticationSuccessEvent event) {
        loginLogger.info("{} logged in", event.getAuthentication().getName());
    }


}
