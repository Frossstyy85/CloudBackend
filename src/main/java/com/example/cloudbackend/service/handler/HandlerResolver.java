package com.example.cloudbackend.service.handler;

import com.example.cloudbackend.domain.Provider;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;

@Service
public class HandlerResolver {

    private final Map<Provider, OAuthHandler> handlerMap;

    public HandlerResolver(List<OAuthHandler> handlers) {
        handlerMap = handlers.stream()
                .collect(Collectors.toMap(OAuthHandler::supports, handler -> handler));
    }

    public OAuthHandler getHandler(Provider provider) {
        OAuthHandler handler = handlerMap.get(provider);
        if (handler == null) {
            throw new IllegalArgumentException("No handler found for provider " + provider);
        }
        return handler;
    }


}
