package com.ntv.ntvcons_backend.entities;

import lombok.*;

import javax.persistence.*;

@Builder
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
    private Integer userId;

    @Column(name = "roleId", nullable = false)
    private Integer roleId;

    @Column(name = "username", nullable = false, length = 50, unique = true)
    private String username;

    @Column(name = "phone", nullable = false, length = 15, unique = true)
    private String phone;

    @Column(name = "email", length = 320)
    private String email;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}