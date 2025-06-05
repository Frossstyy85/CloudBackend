package com.example.cloudbackend.service.oauth2;

import com.example.cloudbackend.domain.Provider;
import com.example.cloudbackend.domain.User;
import com.example.cloudbackend.service.UserService;
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
        Provider provider = Provider.fromString(userRequest.getClientRegistration().getRegistrationId());
        var info = new OAuth2Info(oidc.getGivenName(), oidc.getEmail(), oidc.getPicture(), provider);
        User user = userService.findOrCreate(info);
        return new MultiAuthUser(user.getEmail(), oidc.getAttributes(), user.getId());
    }

}
