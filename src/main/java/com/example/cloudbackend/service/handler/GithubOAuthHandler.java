package com.example.cloudbackend.service.handler;

import com.example.cloudbackend.domain.Provider;
import com.example.cloudbackend.domain.User;
import com.example.cloudbackend.service.OAuth2.GithubEmailFetcher;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GithubOAuthHandler implements OAuthHandler {

    private final GithubEmailFetcher githubEmailFetcher;
    private final OAuthHandlerService oAuthHandlerService;

    public GithubOAuthHandler(GithubEmailFetcher githubEmailFetcher, OAuthHandlerService oAuthHandlerService) {
        this.githubEmailFetcher = githubEmailFetcher;
        this.oAuthHandlerService = oAuthHandlerService;
    }

    @Override
    public Provider supports() {
        return Provider.GITHUB;
    }

    @Override
    public OAuth2User handle(OAuth2User oAuth2User, String accessToken) {
        String name = (String) oAuth2User.getAttribute("login");
        String email = githubEmailFetcher.fetchEmail(accessToken);
        String providerId = oAuth2User.getName();

        OauthInfo info = new OauthInfo(name, email, providerId, supports());

        User user = oAuthHandlerService.findUser(info);
        Map<String, Object> attibutes = new HashMap<>(oAuth2User.getAttributes());
        attibutes.put("domain_id", user.getId());

        return new OAuth2User() {
            @Override
            public Map<String, Object> getAttributes() {
                return attibutes;
            }

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority("ROLE_USER"));
            }

            @Override
            public String getName() {
                return user.getEmail();
            }
        };
    }
}

