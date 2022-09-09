package com.ntv.ntvcons_backend.entities.PostModels;

import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileReadDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ShowPostModel {
    private Long postId, postCategoryId;
    private String authorName, postTitle, ownerName, address, scale, postCategoryName, postCategoryDesc;
    private Status status;
    private Double estimatedCost;

    private Long createdBy, updatedBy;
    private LocalDateTime createdAt, updatedAt;
    private Integer totalPage;

    private List<ExternalFileReadDTO> fileList;
}
