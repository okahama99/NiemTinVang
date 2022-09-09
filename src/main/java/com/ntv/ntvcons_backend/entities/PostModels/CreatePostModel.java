package com.ntv.ntvcons_backend.entities.PostModels;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

@Getter
@Setter
public class CreatePostModel {
    private Long postCategoryId;
    private String authorName, postTitle, ownerName, address, scale;
    private Double estimatedCost;

    @JsonIgnore /* No serialize/deserialize */
    @Schema(hidden = true) /* No show on swagger */
    private Long createdBy = null;
}
