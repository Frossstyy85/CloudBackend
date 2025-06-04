package com.example.cloudbackend.Controller;


import com.example.cloudbackend.domain.User;
import com.example.cloudbackend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String profile(Model model, HttpServletRequest request, HttpServletResponse response){
        User user = userService.getAuthenticatedUser(request, response);

        model.addAttribute("user", user);

        return "profile";
    }



}
