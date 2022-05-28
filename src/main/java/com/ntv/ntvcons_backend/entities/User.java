package com.ntv.ntvcons_backend.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId", nullable = false)
    private int userId;
    @Basic
    @Column(name = "RoleId", nullable = false)
    private int roleId;
    @Basic
    @Column(name = "Username", nullable = false, length = 50)
    private String username;
    @Basic
    @Column(name = "Phone", nullable = false, length = 15)
    private String phone;
    @Basic
    @Column(name = "Email", nullable = true, length = 320)
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId && roleId == user.roleId && Objects.equals(username, user.username) && Objects.equals(phone, user.phone) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId, username, phone, email);
    }
}
