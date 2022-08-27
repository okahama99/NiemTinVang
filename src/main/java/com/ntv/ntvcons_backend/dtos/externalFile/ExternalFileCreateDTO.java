package com.ntv.ntvcons_backend.dtos.externalFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ntv.ntvcons_backend.constants.FileType;
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
public class ExternalFileCreateDTO extends BaseCreateDTO {
    @Schema(example = "File xxx dự án yyy") /* Hint for Swagger */
    @Size(max = 100, message = "fileName max length: 100 characters")
    @NotNull(message = "fileName REQUIRED for Create")
    private String fileName;

    @JsonIgnore
    @Size(max = 100, message = "fileNameFirebase max length: 100 characters")
    private String fileNameFirebase = null;

    @Schema(example = "https://imgur.com/gallery/seX0ozh") /* Hint for Swagger */
    @Size(max = 255, message = "fileLink max length: 100 characters")
    @NotNull(message = "fileLink REQUIRED for Create")
    private String fileLink;

    @NotNull(message = "fileType REQUIRED for Create")
    private FileType fileType;
}
