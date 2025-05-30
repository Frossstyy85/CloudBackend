package com.example.cloudbackend.service;

import com.example.cloudbackend.domain.Provider;
import com.example.cloudbackend.domain.User;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService extends OidcUserService {

    private final UserService userService;

    public CustomOidcUserService(UserService userService) {
        this.userService = userService;
    }

    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidc = super.loadUser(userRequest);
        Provider provider = Provider.findProvider(userRequest.getClientRegistration().getRegistrationId());
        OAuth2Info info = new OAuth2Info(oidc.getName(), oidc.getEmail(), provider, oidc.getSubject());
        User user = userService.findByInfo(info);

        return null;
    }


}
