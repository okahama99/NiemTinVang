package com.ntv.ntvcons_backend.entities;

import com.ntv.ntvcons_backend.constants.Status;
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
@Table(name = "external_file_entity_wrapper_pairing")
public class ExternalFileEntityWrapperPairing extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pairingId", nullable = false)
    private Long pairingId;

    @Column(name = "externalFileId", nullable = false)
    private Long externalFileId;

    @Column(name = "entityWrapperId", nullable = false)
    private Long entityWrapperId;

    @Column(name = "status")
    private Status status;

    /** For CREATE only */
    public ExternalFileEntityWrapperPairing(Long externalFileId, Long entityWrapperId) {
        this.externalFileId = externalFileId;
        this.entityWrapperId = entityWrapperId;
        this.status = Status.ACTIVE;
    }
}