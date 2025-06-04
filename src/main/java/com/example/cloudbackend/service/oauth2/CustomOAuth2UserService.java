package com.example.cloudbackend.service.oauth2;

import com.example.cloudbackend.domain.Provider;
import com.example.cloudbackend.domain.User;
import com.example.cloudbackend.service.UserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {


    private final GithubEmailFetcher githubEmailFetcher;
    private final UserService userService;

    public CustomOAuth2UserService(GithubEmailFetcher githubEmailFetcher, UserService userService) {
        this.githubEmailFetcher = githubEmailFetcher;
        this.userService = userService;
    }

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth = super.loadUser(userRequest);
        Provider provider = Provider.fromString(userRequest.getClientRegistration().getRegistrationId());

        String email = switch (provider){
            case DISCORD -> String.valueOf(oauth.getAttributes().get("email"));
            case GITHUB -> githubEmailFetcher.fetchEmail(userRequest.getClientRegistration().getRegistrationId());
            default -> throw new RuntimeException("Unknown provider");
        };
        User user = userService.findOrCreate(email,provider);
        return new MultiAuthUser(email, oauth.getAttributes(), user.getId());
    }



}
