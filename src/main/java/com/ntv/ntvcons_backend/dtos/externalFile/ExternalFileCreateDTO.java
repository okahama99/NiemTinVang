package com.ntv.ntvcons_backend.dtos.externalFile;

import com.ntv.ntvcons_backend.dtos.BaseCreateDTO;
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
public class ExternalFileCreateDTO extends BaseCreateDTO {
    @Schema(example = "File xxx dự án yyy") /* Hint for Swagger */
    @Size(max = 100, message = "fileName max length: 100 characters")
    @NotNull(message = "fileName REQUIRED for Create")
    private String fileName;

    @Schema(example = "https://imgur.com/gallery/seX0ozh") /* Hint for Swagger */
    @Size(max = 100, message = "fileLink max length: 100 characters")
    @NotNull(message = "fileLink REQUIRED for Create")
    private String fileLink;

    @Positive
    @NotNull(message = "fileTypeId REQUIRED for Create")
    private Long fileTypeId;
}
