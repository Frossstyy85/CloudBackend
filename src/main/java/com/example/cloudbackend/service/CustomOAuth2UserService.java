package com.example.cloudbackend.service;

import com.example.cloudbackend.domain.OAuth2Account;
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

    public CustomOAuth2UserService(OAuth2AccountRepository oAuth2AccountRepository, UserRepository userRepository) {
        this.oAuth2AccountRepository = oAuth2AccountRepository;
        this.userRepository = userRepository;
    }

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Provider provider = Provider.findProvider(userRequest.getClientRegistration().getRegistrationId());
        log.info("Provider found: {}", provider.toString());

        String email = switch (provider) {
            case DISCORD -> oAuth2User.getAttribute("github");
            case GITHUB -> oAuth2User.getAttribute("");
            default -> null;
        };

        String name = switch (provider) {
            case DISCORD -> oAuth2User.getAttribute("name");
            case GITHUB -> oAuth2User.getAttribute("login");
            default -> null;
        };

        String providerId = switch (provider) {
            case GITHUB, DISCORD -> Integer.toString(oAuth2User.getAttribute("id"));
            default -> null;
        };

        User user = oAuth2AccountRepository.findByProviderId(providerId)
                .map(OAuth2Account::getUser)
                .orElseGet(() -> userRepository.findByEmail(email)
                        .map(existingUser -> userRepository.save(
                                existingUser.linkOAuthAccount(
                                        new OAuth2Account(email, provider, providerId, existingUser))))
                        .orElseGet(() -> {
                            User newUser = new User(name, email);
                            return userRepository.save(
                                    newUser.linkOAuthAccount(
                                            new OAuth2Account(email, provider, providerId, newUser)));
                        }));



        return oAuth2User;
    }



}
