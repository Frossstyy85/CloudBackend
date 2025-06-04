package com.example.cloudbackend.service.oauth2;

import com.example.cloudbackend.domain.Provider;
import com.example.cloudbackend.domain.User;
import com.example.cloudbackend.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomOidcUserService extends OidcUserService {

    private final UserService userService;

    public CustomOidcUserService(UserService userService) {
        this.userService = userService;
    }

    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidc = super.loadUser(userRequest);
        Provider provider = Provider.fromString(userRequest.getClientRegistration().getRegistrationId());
        OAuth2Info info = new OAuth2Info(oidc.getName(), oidc.getEmail(), provider, oidc.getSubject());
        User user = userService.findByInfo(info);

        Map<String, Object> customAttributes = new HashMap<>(oidc.getAttributes());
        customAttributes.put("local_id", user.getId());

        return new OidcUser() {
            @Override
            public Map<String, Object> getClaims() {
                return oidc.getClaims();
            }

            @Override
            public OidcUserInfo getUserInfo() {
                return oidc.getUserInfo();
            }

            @Override
            public OidcIdToken getIdToken() {
                return oidc.getIdToken();
            }

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
