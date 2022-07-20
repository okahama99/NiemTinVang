package com.ntv.ntvcons_backend.entities.PostModels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePostModel {
    private Long postCategoryId;
    private String authorName, postTitle, ownerName, address, scale;
}
