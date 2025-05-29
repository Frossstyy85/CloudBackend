package com.example.cloudbackend.service.handler;

import com.example.cloudbackend.domain.OAuthAccount;
import com.example.cloudbackend.domain.User;
import com.example.cloudbackend.repository.OAuthAccountRepository;
import com.example.cloudbackend.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class OAuthHandlerService {

    private final OAuthAccountRepository oAuthAccountRepository;
    private final UserRepository userRepository;

    public OAuthHandlerService(OAuthAccountRepository oAuthAccountRepository, UserRepository userRepository) {
        this.oAuthAccountRepository = oAuthAccountRepository;
        this.userRepository = userRepository;
    }

    public User findUser(OauthInfo info){
        return oAuthAccountRepository.findByProviderId(info.providerId()).map(OAuthAccount::getUser)
                .orElseGet(() -> createOrLinkUser(info));
    }


    private User createOrLinkUser(OauthInfo info) {
        OAuthAccount account = OAuthAccount.builder()
                .provider(info.provider())
                .providerId(info.providerId())
                .email(info.email())
                .build();

        return userRepository.findByEmail(info.email())
                .map(user -> {
                    account.setUser(user);
                    user.addOAuthAccount(account);
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    User user = User.builder()
                            .email(info.email())
                            .name(info.name())
                            .build();
                    account.setUser(user);
                    user.addOAuthAccount(account);
                    return userRepository.save(user);
                });
    }



}
