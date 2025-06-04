package com.example.cloudbackend.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class LoginLogger {

    private static final Logger logger = LogManager.getLogger("Login");

    @EventListener
    public void logLogin(AuthenticationSuccessEvent event){
        logger.info("User {} logged in", event.getAuthentication().getName());
    }



}
