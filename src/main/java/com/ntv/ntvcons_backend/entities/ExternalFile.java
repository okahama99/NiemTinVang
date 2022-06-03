package com.ntv.ntvcons_backend.entities;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "external_file")
public class ExternalFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fileId", nullable = false)
    private Integer fileId;

    @Column(name = "fileName", nullable = false, length = 100)
    private String fileName;

    @Column(name = "fileLink", nullable = false, length = 500, unique = true)
    private String fileLink;

    @Column(name = "fileTypeId", nullable = false)
    private Integer fileTypeId;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}