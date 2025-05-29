package com.example.cloudbackend.service.OAuth2;

import com.example.cloudbackend.domain.OAuthAccount;
import com.example.cloudbackend.domain.Provider;
import com.example.cloudbackend.domain.User;
import com.example.cloudbackend.repository.OAuthAccountRepository;
import com.example.cloudbackend.repository.UserRepository;
import com.example.cloudbackend.service.handler.HandlerResolver;
import com.example.cloudbackend.service.handler.OAuthHandler;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;



@Service
public class CustomOauth2Service extends DefaultOAuth2UserService {

    private final HandlerResolver handlerResolver;

    public CustomOauth2Service(HandlerResolver handlerResolver) {
        this.handlerResolver = handlerResolver;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Provider provider = Provider.findProvider(userRequest.getClientRegistration().getRegistrationId());
        OAuthHandler handler = handlerResolver.getHandler(provider);
        return handler.handle(super.loadUser(userRequest), userRequest.getAccessToken().getTokenValue());
    }


}