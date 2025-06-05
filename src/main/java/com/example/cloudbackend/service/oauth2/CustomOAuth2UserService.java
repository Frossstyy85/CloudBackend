package com.example.cloudbackend.service.oauth2;

import com.example.cloudbackend.domain.Provider;
import com.example.cloudbackend.domain.User;
import com.example.cloudbackend.service.UserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final GithubEmailFetcher githubEmailFetcher;
    private final UserService userService;

    public CustomOAuth2UserService(GithubEmailFetcher githubEmailFetcher, UserService userService) {
        this.githubEmailFetcher = githubEmailFetcher;
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth = super.loadUser(userRequest);
        Provider provider = Provider.fromString(userRequest.getClientRegistration().getRegistrationId());
        Map<String, Object> attributes = oauth.getAttributes();

        String email = switch (provider) {
            case DISCORD -> String.valueOf(attributes.get("email"));
            case GITHUB -> githubEmailFetcher.fetchEmail(userRequest.getAccessToken().getTokenValue());
            default -> null;
        };

        String name = switch (provider) {
            case DISCORD -> String.valueOf(attributes.get("username"));
            case GITHUB -> String.valueOf(attributes.get("login"));
            default -> null;
        };

        String avatar = switch (provider) {
            case DISCORD -> {
                Object id = attributes.get("id");
                Object avatarHash = attributes.get("avatar");
                yield (id != null && avatarHash != null)
                        ? "https://cdn.discordapp.com/avatars/" + id + "/" + avatarHash + ".png"
                        : null;
            }
            case GITHUB -> String.valueOf(attributes.get("avatar_url"));
            default -> null;
        };

        OAuth2Info info = new OAuth2Info(name, email, avatar, provider);
        User user = userService.findOrCreate(info);

        return new MultiAuthUser(email, attributes, user.getId());
    }
}