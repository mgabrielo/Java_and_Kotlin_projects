package com.springsecurity.authsystem.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/login")
    public String Login(){
        return "login";
    }

    @GetMapping("/")
    public String home(){
        return "index";
    }


}
