package com.jwtdev.userrolemanagement.service;


import com.jwtdev.userrolemanagement.dao.RoleDao;
import com.jwtdev.userrolemanagement.dao.UserDao;
import com.jwtdev.userrolemanagement.entity.Role;
import com.jwtdev.userrolemanagement.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserDao userDao;

    @Autowired
    private final RoleDao roleDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerNewUser(User user){
      Role role=  roleDao.findById("User").get();
      Set<Role> roles = new HashSet<>();
      roles.add(role);
      user.setRole(roles);
      user.setUserPassword(getEncodedPassword(user.getUserPassword()));
        return userDao.save(user);
    }


    public void initRolesAndUser(){
        Role adminRole = new Role();
        adminRole.setRoleName("Admin");
        adminRole.setRoleDescription("Admin Role");
        roleDao.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("User");
        userRole.setRoleDescription("Default User Role");
        roleDao.save(userRole);

        User adminUser  = new User();
        adminUser.setUserFirstName("first admin");
        adminUser.setUserLastName("last admin");
        adminUser.setUserName("admin123");
        adminUser.setUserPassword(getEncodedPassword("admin@password"));
        Set<Role> adminSetRoles = new HashSet<>();
        adminSetRoles.add(adminRole);
        adminUser.setRole(adminSetRoles);
        userDao.save(adminUser);

        User user  = new User();
        user.setUserFirstName("grey");
        user.setUserLastName("scott");
        user.setUserName("grey123");
        user.setUserPassword(getEncodedPassword("grey@123"));
        Set<Role> userSetRoles = new HashSet<>();
        userSetRoles.add(userRole);
        user.setRole(userSetRoles);
        userDao.save(user);
    }

    public String getEncodedPassword(String password){
        return passwordEncoder.encode(password);
    }
}
