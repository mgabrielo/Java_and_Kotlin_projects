package com.jwtdev.userrolemanagement.service;

import com.jwtdev.userrolemanagement.dao.RoleDao;
import com.jwtdev.userrolemanagement.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    @Autowired
    private final RoleDao roleDao;

    public Role createNewRole(Role role){
        return roleDao.save(role);
    }
}
