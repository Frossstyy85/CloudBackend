package com.example.cloudbackend.service.handler;

import com.example.cloudbackend.domain.Provider;
import com.example.cloudbackend.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DiscordOAuthHandler implements OAuthHandler{

    private final OAuthHandlerService oAuthHandlerService;

    public DiscordOAuthHandler(OAuthHandlerService oAuthHandlerService) {
        this.oAuthHandlerService = oAuthHandlerService;
    }

    @Override
    public OAuth2User handle(OAuth2User oAuth2User, String accessToken) {
        String name = (String) oAuth2User.getAttribute("name");
        String email = (String) oAuth2User.getAttribute("email");
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

    @Override
    public Provider supports() {
        return Provider.DISCORD;
    }

}
