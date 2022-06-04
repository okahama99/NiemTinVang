package com.ntv.ntvcons_backend.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class ExternalFileEntityWrapperPairing extends BaseEntity {
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
