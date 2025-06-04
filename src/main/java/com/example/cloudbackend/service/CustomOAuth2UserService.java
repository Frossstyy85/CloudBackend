package com.example.cloudbackend.service;

import com.example.cloudbackend.domain.Provider;
import com.example.cloudbackend.domain.User;
import com.example.cloudbackend.repository.OAuth2AccountRepository;
import com.example.cloudbackend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger log = LoggerFactory.getLogger(CustomOAuth2UserService.class);
    private final OAuth2AccountRepository oAuth2AccountRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public CustomOAuth2UserService(OAuth2AccountRepository oAuth2AccountRepository, UserRepository userRepository, UserService userService) {
        this.oAuth2AccountRepository = oAuth2AccountRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Provider provider = Provider.fromString(userRequest.getClientRegistration().getRegistrationId());
        log.info("Provider found: {}", provider.toString());

        String email = switch (provider) {
            case DISCORD -> oAuth2User.getAttribute("github");
            case GITHUB -> oAuth2User.getAttribute("email");
            default -> null;
        };

        String name = switch (provider) {
            case DISCORD -> oAuth2User.getAttribute("name");
            case GITHUB -> oAuth2User.getAttribute("login");
            default -> null;
        };

        String providerId = oAuth2User.getAttribute("id").toString();

        OAuth2Info info = new OAuth2Info(name, email, provider, providerId);

        User user = userService.findByInfo(info);

        System.out.println(user);

        return oAuth2User;
    }



}
