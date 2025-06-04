package com.example.cloudbackend.domain;

import com.example.cloudbackend.exception.ProviderNotFoundException;

public enum Provider {

    GITHUB,
    GOOGLE,
    DISCORD,
    LOCAL;

    public static Provider fromString(String providerName) {
        if (providerName == null) {
            throw new ProviderNotFoundException("null");
        }

        try {
            return Provider.valueOf(providerName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ProviderNotFoundException(providerName);
        }
    }
}
