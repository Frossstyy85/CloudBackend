package com.example.cloudbackend.service.oauth2;

import com.example.cloudbackend.domain.Provider;

public record OAuth2Info(String name, String email, String avatar, Provider provider) {
}
