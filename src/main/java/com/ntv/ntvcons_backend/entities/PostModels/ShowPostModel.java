package com.ntv.ntvcons_backend.entities.PostModels;

import com.ntv.ntvcons_backend.constants.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ShowPostModel {
    private Long postId, postCategoryId;
    private String authorName, postTitle, ownerName, address, scale, postCategoryName, postCategoryDesc;
    private Status status;

    private Long createdBy, updatedBy;
    private LocalDateTime createdAt, updatedAt;
    private Integer totalPage;
}
