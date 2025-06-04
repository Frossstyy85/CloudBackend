package com.example.cloudbackend.logging.aws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class LoginLogger {

    private final Logger logger = LoggerFactory.getLogger("login-logger");

    @EventListener
    public void logLogin(AuthenticationSuccessEvent event){
        logger.info("User {} logged in", event.getAuthentication().getName());
    }



}
