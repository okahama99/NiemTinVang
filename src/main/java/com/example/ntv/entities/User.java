package com.example.ntv.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId", nullable = false)
    private int userId;
    @Basic
    @Column(name = "RoleId", nullable = false)
    private int roleId;
    @Basic
    @Column(name = "Username", nullable = false)
    private String username;
    @Basic
    @Column(name = "Phone", nullable = false)
    private String phone;
    @Basic
    @Column(name = "Email")
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