package com.ntv.ntvcons_backend.dtos.externalFile;

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
public class ExternalFileUpdateDTO extends BaseUpdateDTO {
    @Positive
    @NotNull(message = "Id REQUIRED for update")
    private Long fileId;

    @ApiModelProperty(example = "File xxx dự án yyy") /* Hint for Swagger */
    @Size(max = 100, message = "fileName max length: 100 characters")
    @NotNull(message = "fileName REQUIRED for update")
    private String fileName;

    @ApiModelProperty(example = "https://imgur.com/gallery/seX0ozh") /* Hint for Swagger */
    @Size(max = 100, message = "fileLink max length: 100 characters")
    @NotNull(message = "fileLink REQUIRED for update")
    private String fileLink;

    @Positive
    @NotNull(message = "fileTypeId REQUIRED for update")
    private Long fileTypeId;
}
