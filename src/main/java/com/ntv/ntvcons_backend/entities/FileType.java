package com.ntv.ntvcons_backend.entities;

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
@Table(name = "file_type")
public class FileType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fileTypeId", nullable = false)
    private Long fileTypeId;

    @Column(name = "fileTypeName", nullable = false, length = 100)
    private String fileTypeName;

    @Column(name = "fileTypeDesc", length = 100)
    private String fileTypeDesc;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}