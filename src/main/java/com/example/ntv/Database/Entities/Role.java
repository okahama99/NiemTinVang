package com.example.ntv.Database.Entities;

import com.example.ntv.Database.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;


@Getter
@Setter
@Entity
@Table(name = "role")
public class Role extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoleId", nullable = false)
    private int roleId;
    @Basic
    @Column(name = "RoleName", nullable = false)
    private String roleName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return roleId == role.roleId && Objects.equals(roleName, role.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, roleName);
    }
}
