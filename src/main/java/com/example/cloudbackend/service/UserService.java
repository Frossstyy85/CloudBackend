package com.example.cloudbackend.service;

import com.example.cloudbackend.domain.OAuth2Account;
import com.example.cloudbackend.domain.User;
import com.example.cloudbackend.repository.OAuth2AccountRepository;
import com.example.cloudbackend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final OAuth2AccountRepository oAuth2AccountRepository;

    private final UserRepository userRepository;

    public UserService(OAuth2AccountRepository oAuth2AccountRepository, UserRepository userRepository) {
        this.oAuth2AccountRepository = oAuth2AccountRepository;
        this.userRepository = userRepository;
    }

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new User("test", "test@gmail.com");
    }

    @Transactional
    public User findByInfo(OAuth2Info info) {
        return oAuth2AccountRepository.findByProviderId(info.providerId())
                .map(OAuth2Account::getUser)
                .orElseGet(() -> handleFirstTimeLogin(info));
    }

    private User handleFirstTimeLogin(OAuth2Info info) {
        User user = getOrCreateUser(info);
        System.out.println("hello");

        return linkExternalAccount(user, info);
    }

    private User getOrCreateUser(OAuth2Info info) {
        return findByEmail(info.email())
                .orElseGet(() -> new User(info.name(), info.email()));
    }

    private User linkExternalAccount(User user, OAuth2Info info) {
        OAuth2Account newAccount = new OAuth2Account(info.email(), info.provider(), info.providerId(), user);
        user.linkOAuthAccount(newAccount);
        return saveUser(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        log.info("Saving user to database: {}", user);
        return userRepository.save(user);
    }


}
