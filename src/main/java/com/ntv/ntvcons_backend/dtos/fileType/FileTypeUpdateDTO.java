package com.ntv.ntvcons_backend.dtos.fileType;

import com.ntv.ntvcons_backend.dtos.BaseUpdateDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileTypeUpdateDTO extends BaseUpdateDTO {
    @Positive
    @NotNull(message = "Id REQUIRED for update")
    private Long fileTypeId;

    @ApiModelProperty(example = "Ảnh PNG") /* Hint for Swagger */
    @Size(max = 100, message = "fileTypeName max length: 100 characters")
    @NotNull(message = "fileTypeName REQUIRED for update")
    private String fileTypeName;

    @ApiModelProperty(example = "Ảnh PNG") /* Hint for Swagger */
    @Size(max = 100, message = "fileTypeDesc max length: 100 characters")
    private String fileTypeDesc;

    @ApiModelProperty(example = "png") /* Hint for Swagger */
    @Size(max = 10, message = "fileTypeExtension max length: 10 characters")
    @NotNull(message = "fileTypeExtension REQUIRED for update")
    private String fileTypeExtension;
}
