package com.ntv.ntvcons_backend.entities;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "blueprint")
public class Blueprint extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blueprintId", nullable = false)
    private Long blueprintId;

    @Column(name = "designerId", nullable = false)
    private Long designerId;

    @Column(name = "blueprintName", nullable = false, length = 500)
    private String blueprintName;

    @Column(name = "estimatedCost", nullable = false)
    private Double estimatedCost;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}