package com.ntv.ntvcons_backend.dtos.post;

import com.ntv.ntvcons_backend.dtos.BaseUpdateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateDTO extends BaseUpdateDTO {
    @Positive
    @NotNull(message = "Id REQUIRED for Update")
    private Long postId;

    @Schema(example = "Nguyen Van A") /* Hint for Swagger */
    @Size(max = 100, message = "authorName max length: 100 characters")
    @NotNull(message = "authorName REQUIRED for Update")
    private String authorName;

    @Schema(example = "Giá xây dựng 2022") /* Hint for Swagger */
    @Size(max = 100, message = "postTitle max length: 100 characters")
    @NotNull(message = "postTitle REQUIRED for Update")
    private String postTitle;
}
