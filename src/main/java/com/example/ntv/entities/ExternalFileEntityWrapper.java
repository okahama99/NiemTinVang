package com.example.ntv.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "externalfileentitywrapperpairing")
class ExternalFileEntityWrapperPairing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PairingId", nullable = false)
    private int pairingId;
    @Basic
    @Column(name = "ExternalFileId", nullable = false)
    private int externalFileId;
    @Basic
    @Column(name = "EntityWrapperId", nullable = false)
    private int entityWrapperId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExternalFileEntityWrapperPairing that = (ExternalFileEntityWrapperPairing) o;
        return pairingId == that.pairingId && externalFileId == that.externalFileId && entityWrapperId == that.entityWrapperId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pairingId, externalFileId, entityWrapperId);
    }
}