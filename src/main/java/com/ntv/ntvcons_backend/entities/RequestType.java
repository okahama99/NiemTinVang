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
@Table(name = "request_type")
public class RequestType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "requestTypeId", nullable = false)
    private Long requestTypeId;

    @Column(name = "requestTypeName", nullable = false, length = 50)
    private String requestTypeName;

    @Column(name = "requestTypeDesc", length = 100)
    private String requestTypeDesc;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}