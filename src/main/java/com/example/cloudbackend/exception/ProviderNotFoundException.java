package com.example.cloudbackend.exception;

public class ProviderNotFoundException extends RuntimeException {

    public ProviderNotFoundException(String message){
        super("Provider not found for: " + message);
    }


}
