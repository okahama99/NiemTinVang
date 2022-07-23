package com.ntv.ntvcons_backend.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "roleId", nullable = false)
    private Long roleId;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "fullName", nullable = false, length = 100)
    private String fullName;

    @Column(name = "password", length = 128)
    private String password;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "email", length = 320)
    private String email;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}