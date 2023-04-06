package com.jwtdev.userrolemanagement.controller;

import com.jwtdev.userrolemanagement.dto.JwtRequest;
import com.jwtdev.userrolemanagement.dto.JwtResponse;
import com.jwtdev.userrolemanagement.service.JwtService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class JwtController {
    @Autowired
    private final JwtService jwtService;

    @PostMapping("/authenticate")
    public JwtResponse createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        return jwtService.createJwtToken(jwtRequest);
    }

}
