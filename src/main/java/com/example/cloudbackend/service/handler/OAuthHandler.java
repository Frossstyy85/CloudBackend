package com.example.cloudbackend.service.handler;

import com.example.cloudbackend.domain.Provider;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuthHandler {


    public OAuth2User handle(OAuth2User oAuth2User, String accessToken);


    public Provider supports();


}
