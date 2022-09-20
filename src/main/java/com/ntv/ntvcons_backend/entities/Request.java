package com.ntv.ntvcons_backend.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private Long requestId;

    @Column(name = "projectId", nullable = false)
    private Long projectId;

    @Column(name = "requestTypeId", nullable = false)
    private Long requestTypeId;

    @Column(name = "requesterId", nullable = false)
    private Long requesterId;

    @Column(name = "requestName")
    private String requestName;

    @Column(name = "requestDate", nullable = false)
    private LocalDateTime requestDate;

    @Column(name = "requestDesc", nullable = false, length = 100)
    private String requestDesc;

    @Column(name = "isVerified", nullable = false)
    private Boolean isVerified = false;

    @Column(name = "verifierId")
    private Long verifierId;

    @Column(name = "verifyDate")
    private LocalDateTime verifyDate;

    @Column(name = "verifyNote", length = 100)
    private String verifyNote;

    @Column(name = "isApproved")
    private Boolean isApproved;

}