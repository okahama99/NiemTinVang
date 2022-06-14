package com.ntv.ntvcons_backend.entities;

import lombok.*;

//import org.springframework.data.relational.core.mapping.Table;

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

    @Column(name = "designerName", nullable = false)
    private String designerName; // t đổi qua designerName r nè

    @Column(name = "blueprintName", nullable = false, length = 100)
    private String blueprintName;

    @Column(name = "estimatedCost", nullable = false)
    private Double estimatedCost;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}