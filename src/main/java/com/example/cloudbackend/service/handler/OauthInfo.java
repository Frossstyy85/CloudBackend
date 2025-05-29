package com.example.cloudbackend.service.handler;

import com.example.cloudbackend.domain.Provider;

public record OauthInfo(String name, String email, String providerId, Provider provider) {
}
