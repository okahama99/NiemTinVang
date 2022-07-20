package com.ntv.ntvcons_backend.entities.PostCategoryModels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePostCategoryModel {
    private Long postCategoryId;
    private String postCategoryName, postCategoryDesc;
}
