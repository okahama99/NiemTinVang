package com.ntv.ntvcons_backend.entities.PostCategoryModels;

import com.ntv.ntvcons_backend.constants.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ShowPostCategoryModel {
    private Long postCategoryId;
    private String postCategoryName, postCategoryDesc;
    private Status status;

    private Long createdBy, updatedBy;
    private LocalDateTime createdAt, updatedAt;
    private Integer totalPage;
}
