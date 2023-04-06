package com.jwtdev.userrolemanagement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import javax.validation.constraints.NotNull;

@Entity
public class Role {

    @Id
    @Column(length = 100, unique=true,nullable = false)

    private String roleName;
    @Column(nullable = false)
    private String roleDescription;


    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }
}
