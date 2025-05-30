package com.example.cloudbackend.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping( "/")

public class ViewController {

    @GetMapping
    public String home(){
        return "index";
    }

    @GetMapping("/profile")
    public String profile(){
        return "profile";
    }



}
