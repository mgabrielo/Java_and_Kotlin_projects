package com.jwtdev.userrolemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
public class UserRoleManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserRoleManagementApplication.class, args);
	}

}
