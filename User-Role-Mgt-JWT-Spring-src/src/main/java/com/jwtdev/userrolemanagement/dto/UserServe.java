package com.jwtdev.userrolemanagement.dto;

import com.jwtdev.userrolemanagement.dao.RoleDao;
import com.jwtdev.userrolemanagement.dao.UserDao;
import com.jwtdev.userrolemanagement.entity.Role;
import com.jwtdev.userrolemanagement.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServe {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

  //  @Autowired
   // private PasswordEncoder passwordEncoder;
    public User registerNewUser(User user){
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
        adminUser.setUserPassword("admin@password");
        Set<Role> adminSetRoles = new HashSet<>();
        adminSetRoles.add(adminRole);
        adminUser.setRole(adminSetRoles);
        userDao.save(adminUser);

        User user  = new User();
        user.setUserFirstName("grey");
        user.setUserLastName("scott");
        user.setUserName("grey123");
        user.setUserPassword("grey@123");
        Set<Role> userSetRoles = new HashSet<>();
        userSetRoles.add(userRole);
        user.setRole(userSetRoles);
        userDao.save(user);
    }


}
