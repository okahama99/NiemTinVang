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
@Table(name = "external_file")
public class ExternalFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fileId", nullable = false)
    private Long fileId;

    @Column(name = "fileTypeId", nullable = false)
    private Long fileTypeId;

    @Column(name = "fileName", nullable = false, length = 100)
    private String fileName;

    @Column(name = "fileLink", nullable = false, length = 100, unique = true)
    private String fileLink;

    @Column(name = "status")
    private Status status;

}