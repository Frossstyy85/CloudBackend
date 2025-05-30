package com.example.cloudbackend.domain;

import com.example.cloudbackend.exception.ProviderNotFoundException;

import java.util.Optional;

public enum Provider {

    GITHUB,
    GOOGLE,
    DISCORD;

    public static Provider findProvider(String providerName){
        return Optional.ofNullable(providerName)
                .map(String::toUpperCase)
                .map(name -> {
                    try {
                        return Provider.valueOf(name);
                    } catch (IllegalArgumentException e) {
                        throw new ProviderNotFoundException(providerName);
                    }
                })
                .orElseThrow(() -> new ProviderNotFoundException("null"));
    }



}
