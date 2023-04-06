package com.jwtdev.userrolemanagement.controller;

import com.jwtdev.userrolemanagement.entity.User;
import com.jwtdev.userrolemanagement.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserController {

    private final UserService userService;

    @PostConstruct
    public void initRoleAndUsers(){
        userService.initRolesAndUser();
    }

    @PostMapping("/registerNewUser")
    public User registerNewUser(@RequestBody User user){
        return userService.registerNewUser(user);
    }

    @GetMapping("/forAdmin")
   @PreAuthorize("hasRole('Admin')")
    public String forAdmin(){
        return "Only Accessible by Admins";
    }

    @GetMapping("/forUser")
   @PreAuthorize("hasRole('User')")
    public String forUser(){
        return "Only Accessible by Users";
    }
}
