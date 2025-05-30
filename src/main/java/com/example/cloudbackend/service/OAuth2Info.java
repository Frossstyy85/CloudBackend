package com.example.cloudbackend.service;

import com.example.cloudbackend.domain.Provider;

public record OAuth2Info(String name, String email, Provider provider, String providerId) {
}
