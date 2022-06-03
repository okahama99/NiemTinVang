package com.ntv.ntvcons_backend.entities;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project_blueprint")
public class ProjectBlueprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blueprintId", nullable = false)
    private Integer blueprintId;

    @Column(name = "blueprintName", nullable = false, length = 500)
    private String blueprintName;

    @Column(name = "designerId", nullable = false)
    private Integer designerId;

    @Column(name = "estimatedCost", nullable = false)
    private Double estimatedCost;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}