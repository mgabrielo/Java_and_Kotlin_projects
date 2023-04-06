package com.jwtdev.userrolemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

    private String userName;
    private String userFirstName;
    private String userLastName;
    private String userPassword;
}
