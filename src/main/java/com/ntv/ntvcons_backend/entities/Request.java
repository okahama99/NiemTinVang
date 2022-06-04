package com.ntv.ntvcons_backend.entities;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "request")
public class Request extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "requestId", nullable = false)
    private Integer requestId;

    @Column(name = "projectId", nullable = false)
    private Integer projectId;

    @Column(name = "requestTypeId", nullable = false)
    private Integer requestTypeId;

    @Column(name = "RequesterId", nullable = false)
    private Integer RequesterId;

    @Column(name = "requestDate", nullable = false)
    private Instant requestDate;

    @Column(name = "requestDesc", nullable = false, length = 500)
    private String requestDesc;

    @Column(name = "verifierId")
    private Integer verifierId;

    @Column(name = "isVerified", nullable = false)
    private Boolean isVerified = false;

    @Column(name = "verifyDate")
    private Instant verifyDate;

    @Column(name = "verifyNote", length = 500)
    private String verifyNote;

    @Column(name = "isApproved")
    private Boolean isApproved;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}