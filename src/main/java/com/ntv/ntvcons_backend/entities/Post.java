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
@Table(name = "post")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postId", nullable = false)
    private Long postId;

    @Column(name = "postCategoryId", nullable = false)
    private Long postCategoryId;

    @Column(name = "authorName", nullable = false, length = 100)
    private String authorName;

    @Column(name = "postTitle", nullable = false, length = 100)
    private String postTitle;

    @Column(name = "ownerName", nullable = false, length = 100)
    private String ownerName;

    @Column(name = "address", nullable = false, length = 100)
    private String address;

    @Column(name = "estimatedCost")
    private Double estimatedCost;

    @Column(name = "scale", nullable = false, length = 100)
    private String scale;
}