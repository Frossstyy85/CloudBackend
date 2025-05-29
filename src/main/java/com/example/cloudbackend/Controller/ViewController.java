package com.example.cloudbackend.Controller;

import com.example.cloudbackend.domain.OAuthAccount;
import com.example.cloudbackend.domain.Provider;
import com.example.cloudbackend.domain.User;
import com.example.cloudbackend.repository.UserRepository;
import com.example.cloudbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.EnumMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping( "/")

public class ViewController {


    private final UserRepository userRepository;
    private final UserService userService;

    public ViewController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping
    public String index(){
        return "index";
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication){
        User user = userService.getAuthenticatedUser();

        model.addAttribute("user", user);
        model.addAttribute("providers",Provider.values());
        return "profile";
    }


}
