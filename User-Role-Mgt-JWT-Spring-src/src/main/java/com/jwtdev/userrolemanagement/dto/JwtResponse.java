package com.jwtdev.userrolemanagement.dto;

import com.jwtdev.userrolemanagement.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {

    private User user;
    private String jwtToken;
}
