package com.example.cloudbackend.controller;


import com.example.cloudbackend.domain.User;
import com.example.cloudbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping( "/")

public class ViewController {

    private final UserService userService;

    public ViewController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String home(){
        return "index";
    }

    @GetMapping("/profile")
    public String profile(Model model){

        User user = userService.getCurrentUser();

        model.addAttribute("user", user);

        return "profile";
    }



}
