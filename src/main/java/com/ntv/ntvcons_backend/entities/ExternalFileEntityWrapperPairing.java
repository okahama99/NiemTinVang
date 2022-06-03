package com.ntv.ntvcons_backend.entities;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "external_file_entity_wrapper_pairing")
public class ExternalFileEntityWrapperPairing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pairingId", nullable = false)
    private Integer pairingId;

    @Column(name = "externalFileId", nullable = false)
    private Integer externalFileId;

    @Column(name = "entityWrapperId", nullable = false)
    private Integer entityWrapperId;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}