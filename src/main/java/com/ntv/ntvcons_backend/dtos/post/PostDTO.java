package com.ntv.ntvcons_backend.dtos.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO implements Serializable {
    private Integer postId;
    private String author;
    private String postTitle;
    private LocalDateTime createDate;
    private Boolean isDeleted = false;
}
