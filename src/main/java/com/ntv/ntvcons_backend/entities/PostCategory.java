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
@Table(name = "post_category")
public class PostCategory extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postCategoryId", nullable = false)
    private Long postCategoryId;

    @Column(name = "postCategoryName", nullable = false, length = 100)
    private String postCategoryName;

    @Column(name = "postCategoryDesc", nullable = false, length = 100)
    private String postCategoryDesc;

    @Column(name = "status", nullable = false, length = 100)
    private Status status;
}
