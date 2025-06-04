package com.example.cloudbackend.service;

import com.example.cloudbackend.domain.OAuth2Account;
import com.example.cloudbackend.domain.User;
import com.example.cloudbackend.repository.OAuth2AccountRepository;
import com.example.cloudbackend.repository.UserRepository;
import com.example.cloudbackend.service.oauth2.OAuth2Info;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final OAuth2AccountRepository oAuth2Repo;
    private final UserRepository userRepo;

    public UserService(OAuth2AccountRepository oAuth2Repo, UserRepository userRepo) {
        this.oAuth2Repo = oAuth2Repo;
        this.userRepo = userRepo;
    }

    public User getAuthenticatedUser(HttpServletRequest request, HttpServletResponse response) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            logout(request, response);
            return null;
        }
        Object principal = authentication.getPrincipal();

        Long id;
        if (principal instanceof OAuth2User oauth) {
            Object localIdAttr = oauth.getAttribute("local_id");
            if (localIdAttr == null) {
                logout(request, response);
                throw new RuntimeException("User id attribute is missing");
            }
            try {
                if (localIdAttr instanceof Number) {
                    id = ((Number) localIdAttr).longValue();
                } else {
                    id = Long.parseLong(localIdAttr.toString());
                }
            } catch (NumberFormatException e) {
                logout(request, response);
                throw new RuntimeException("User id attribute is invalid", e);
            }
        } else {
            logout(request, response);
            return null;
        }

        return findById(id).orElseThrow(() -> {
            logout(request, response);
            return new RuntimeException("User not found");
        });
    }



    @Transactional
    public User findByInfo(OAuth2Info info) {
        return oAuth2Repo.findByProviderId(info.providerId())
                .map(OAuth2Account::getUser)
                .orElseGet(() -> {
                    User user = findByEmail(info.email())
                            .orElseGet(() -> new User(info.name(), info.email()));

                    OAuth2Account account = new OAuth2Account(
                            info.email(), info.provider(), info.providerId(), user);
                    user.linkOAuthAccount(account);

                    return saveUser(user);
                });
    }


    public Optional<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User saveUser(User user) {
        return userRepo.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }


    public void logout(HttpServletRequest request, HttpServletResponse response) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
    }




}

