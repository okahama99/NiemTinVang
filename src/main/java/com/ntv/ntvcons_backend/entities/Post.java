package com.ntv.ntvcons_backend.entities;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postId", nullable = false)
    private Integer postId;

    @Column(name = "authorId", nullable = false)
    private Integer authorId;

    @Column(name = "postTitle", nullable = false, length = 500)
    private String postTitle;

    @Column(name = "createDate", nullable = false)
    private Instant createDate;

    @Column(name = "isDeleted", nullable = false)
    private Boolean isDeleted = false;

}