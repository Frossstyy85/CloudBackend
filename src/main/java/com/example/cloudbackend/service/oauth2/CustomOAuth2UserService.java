package com.example.cloudbackend.service.oauth2;

import com.example.cloudbackend.domain.Provider;
import com.example.cloudbackend.domain.User;
import com.example.cloudbackend.repository.OAuth2AccountRepository;
import com.example.cloudbackend.repository.UserRepository;
import com.example.cloudbackend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final Logger log = LoggerFactory.getLogger(CustomOAuth2UserService.class);
    private final OAuth2AccountRepository oAuth2AccountRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final GithubEmailFetcher githubEmailFetcher;

    public CustomOAuth2UserService(OAuth2AccountRepository oAuth2AccountRepository, UserRepository userRepository, UserService userService, GithubEmailFetcher githubEmailFetcher) {
        this.oAuth2AccountRepository = oAuth2AccountRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.githubEmailFetcher = githubEmailFetcher;
    }

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Provider provider = Provider.fromString(userRequest.getClientRegistration().getRegistrationId());

        String email = switch (provider) {
            case DISCORD -> oAuth2User.getAttribute("email");
            case GITHUB -> githubEmailFetcher.fetchEmail(userRequest.getAccessToken().getTokenValue());
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

        Map<String, Object> customAttributes = new HashMap<>(oAuth2User.getAttributes());
        customAttributes.put("local_id", user.getId());


        return new OAuth2User() {
            @Override
            public Map<String, Object> getAttributes() {
                return customAttributes;
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
            }

            @Override
            public String getName() {
                return user.getEmail();
            }
        };

    }



}
