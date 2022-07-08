package com.ntv.ntvcons_backend.dtos.post;

import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateDTO extends BaseCreateDTO {
    @ApiModelProperty(example = "Nguyen Van A") /* Hint for Swagger */
    @Size(max = 100, message = "authorName max length: 100 characters")
    @NotNull(message = "authorName REQUIRED for create")
    private String authorName;

    @ApiModelProperty(example = "Giá xây dựng 2022") /* Hint for Swagger */
    @Size(max = 100, message = "postTitle max length: 100 characters")
    @NotNull(message = "postTitle REQUIRED for create")
    private String postTitle;
}
