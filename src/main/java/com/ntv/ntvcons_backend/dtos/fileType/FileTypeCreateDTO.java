package com.ntv.ntvcons_backend.dtos.fileType;

import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileTypeCreateDTO extends BaseCreateDTO {
    @Schema(example = "Ảnh PNG") /* Hint for Swagger */
    @Size(max = 100, message = "fileTypeName max length: 100 characters")
    @NotNull(message = "fileTypeName REQUIRED for Create")
    private String fileTypeName;

    @Schema(example = "Ảnh PNG") /* Hint for Swagger */
    @Size(max = 100, message = "fileTypeDesc max length: 100 characters")
    private String fileTypeDesc;
}
