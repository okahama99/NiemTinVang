package com.example.ntv.Database.Entities;
import com.example.ntv.Database.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;


@Getter
@Setter
@Entity
@Table(name = "request")
public class Request extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RequestId", nullable = false)
    private int requestId;
    @Basic
    @Column(name = "ProjectId", nullable = false)
    private int projectId;
    @Basic
    @Column(name = "RequesterId", nullable = false)
    private int requesterId;
    @Basic
    @Column(name = "RequestDatetime", nullable = false)
    private Timestamp requestDatetime;
    @Basic
    @Column(name = "RequestDesc", nullable = false)
    private String requestDesc;
    @Basic
    @Column(name = "VerifierId")
    private Integer verifierId;
    @Basic
    @Column(name = "IsVerified", nullable = false)
    private boolean isVerified = false;
    @Basic
    @Column(name = "IsApproved")
    private Boolean isApproved;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return requestId == request.requestId && projectId == request.projectId && requesterId == request.requesterId && isVerified == request.isVerified && Objects.equals(requestDatetime, request.requestDatetime) && Objects.equals(requestDesc, request.requestDesc) && Objects.equals(verifierId, request.verifierId) && Objects.equals(isApproved, request.isApproved);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, projectId, requesterId, requestDatetime, requestDesc, verifierId, isVerified, isApproved);
    }
}