package com.example.cloudbackend.service;

import com.example.cloudbackend.domain.OAuthAccount;
import com.example.cloudbackend.domain.Provider;
import com.example.cloudbackend.domain.User;
import com.example.cloudbackend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private Long getAuthenticatedUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = ((OAuth2User) authentication.getPrincipal()).getAttribute("domain_id");
        return id;
    }

    public User getAuthenticatedUser(){
        Long id = getAuthenticatedUserId();
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("no authentication found"));
    }

    public void disconnectProvider(String provider){

        Provider prov = Provider.findProvider(provider);
        Long id = getAuthenticatedUserId();
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("idk"));

        Iterator<OAuthAccount> iterator = user.getLinkedAccounts().iterator();
        while (iterator.hasNext()) {
            var account = iterator.next();
            if (account.getProvider().equals(prov)){
                iterator.remove();
                break;
            }
        }

        userRepository.save(user);
    }


}
