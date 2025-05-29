package com.example.cloudbackend.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Provider {

    GOOGLE,
    DISCORD,
    GITHUB;

    private static final Map<String, Provider> lookup = Arrays.stream(values())
            .collect(Collectors.toUnmodifiableMap(
                    Provider::name,
                    p -> p
            ));

    public static Provider findProvider(String s){
        if (s == null) return null;
        Provider provider = lookup.get(s.toUpperCase());
        if (provider == null)
            throw new RuntimeException("No provider found for " + s);
        return provider;
    }


}
