package com.spring.jwtdevtype.controllers;

import com.spring.jwtdevtype.config.JwtUtils;
import com.spring.jwtdevtype.dao.UserDao;
import com.spring.jwtdevtype.dto.AuthenticationRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    @Autowired
    private final AuthenticationManager authenticationManager;

    private final UserDao userDao;

    private final JwtUtils jwtUtils;


    @PostMapping("/new")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest request){
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            final UserDetails user = userDao.findUserByEmail(request.getEmail());

            if(user != null){
                return ResponseEntity.ok(jwtUtils.generateToken(user)) ;
            }

            return ResponseEntity.status(400).body("error has occurred") ;
    }


    @GetMapping("/old")
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hello from our Api");
    }
}
