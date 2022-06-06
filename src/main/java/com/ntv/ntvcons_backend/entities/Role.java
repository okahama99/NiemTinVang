package com.ntv.ntvcons_backend.entities;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "role")
public class Role extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roleId", nullable = false)
    private Long roleId;

    @Column(name = "roleName", nullable = false, length = 50)
    private String roleName;

    @Column(name = "roleDesc", length = 500)
    private String roleDesc;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}