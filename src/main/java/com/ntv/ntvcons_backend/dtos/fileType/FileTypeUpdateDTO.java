package com.ntv.ntvcons_backend.dtos.fileType;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileTypeUpdateDTO implements Serializable {
    private Long fileTypeId;
    private String fileTypeName;
    private String fileTypeDesc;
    private String fileTypeExtension;

    /* TODO: to be replace with status */
    @JsonIgnore /* No serialize/deserialize => no accept input */
    @ApiModelProperty(hidden = true) /* No show on Swagger */
    private final Boolean isDeleted = false;
}
