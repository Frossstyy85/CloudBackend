package com.example.cloudbackend.service;

import com.example.cloudbackend.domain.OAuth2Account;
import com.example.cloudbackend.domain.User;
import com.example.cloudbackend.repository.OAuth2AccountRepository;
import com.example.cloudbackend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private OAuth2AccountRepository oAuth2AccountRepository;

    @Autowired
    private UserRepository userRepository;

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new User("test", "test@gmail.com");
    }


    public User findByInfo(OAuth2Info info) {
            return oAuth2AccountRepository.findByProviderId(info.providerId())
                    .map(OAuth2Account::getUser)
                    .orElseGet(() -> {
                        User user = findOrCreateUserByEmail(info);
                        return linkOAuthAccount(user, info);
                    });
        }

        private User findOrCreateUserByEmail(OAuth2Info info) {
            return userRepository.findByEmail(info.email())
                    .orElseGet(() -> {
                        User newUser = new User(info.name(), info.email());
                        return newUser;
                    });
        }

        private User linkOAuthAccount(User user, OAuth2Info info) {
            OAuth2Account newAccount = new OAuth2Account(info.email(), info.provider(), info.providerId(), user);
            user.linkOAuthAccount(newAccount);
            return saveUser(user);
        }

        public User saveUser(User user) {
            log.info("saving user to database: {}", user);
            return userRepository.save(user);
        }


}
