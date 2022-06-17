package com.ntv.ntvcons_backend.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "post")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postId", nullable = false)
    private Long postId;

    @Column(name = "authorId", nullable = false)
    private Long authorId;

    @Column(name = "postTitle", nullable = false, length = 100)
    private String postTitle;

    @Column(name = "createDate", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}