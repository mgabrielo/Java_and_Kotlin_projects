package com.springsecurity.authsystem.service;

import com.springsecurity.authsystem.dto.UserRegistrationDto;
import com.springsecurity.authsystem.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {
    @Bean
    User save(UserRegistrationDto registrationDto);
}
