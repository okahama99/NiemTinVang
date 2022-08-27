package com.ntv.ntvcons_backend.entities;

import com.ntv.ntvcons_backend.constants.FileType;
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
@Table(name = "external_file")
public class ExternalFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fileId", nullable = false)
    private Long fileId;

    @Column(name = "fileType", length = 50)
    private FileType fileType;

    @Column(name = "fileName", nullable = false, length = 100)
    private String fileName;

    @Column(name = "fileNameFirebase", length = 100)
    private String fileNameFirebase;

    @Column(name = "fileLink", nullable = false, length = 255, unique = true)
    private String fileLink;
}