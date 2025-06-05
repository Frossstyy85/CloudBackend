package com.example.cloudbackend.service;

import com.example.cloudbackend.domain.User;
import com.example.cloudbackend.repository.UserRepository;
import com.example.cloudbackend.service.oauth2.MultiAuthUser;
import com.example.cloudbackend.service.oauth2.OAuth2Info;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findOrCreate(OAuth2Info info) {
        return userRepository.findByEmailAndProvider(info.email(), info.provider())
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .email(info.email())
                                .provider(info.provider())
                                .avatar(info.avatar())
                                .name(info.name())
                                .build()
                ));
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof MultiAuthUser user)  {
            return userRepository.findById(user.getId()).orElse(null);
        }
        throw new RuntimeException("Something went wrong");
    }





}