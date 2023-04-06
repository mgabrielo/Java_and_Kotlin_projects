package com.jwtdev.userrolemanagement.controller;

import com.jwtdev.userrolemanagement.entity.Role;
import com.jwtdev.userrolemanagement.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class RoleController {

    @Autowired
    private final RoleService roleService;

    @PostMapping("/createNewRole")
    public Role createNewRole(@RequestBody Role role){
      return   roleService.createNewRole(role);
    }
}
