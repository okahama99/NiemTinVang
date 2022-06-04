package com.ntv.ntvcons_backend.entities;

import lombok.*;

import javax.persistence.*;

@Builder
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
    private Integer fileTypeId;

    @Column(name = "fileTypeName", nullable = false, length = 100)
    private String fileTypeName;

    @Column(name = "fileTypeDesc", length = 500)
    private String fileTypeDesc;

    @Column(name = "fileTypeExtension", nullable = false, length = 10)
    private String fileTypeExtension;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}