package com.ntv.ntvcons_backend.dtos.projectBlueprint;

import com.ntv.ntvcons_backend.dtos.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectBlueprintDTO implements Serializable {
    private Integer blueprintId;
    private String blueprintName;
    private UserDTO designer;
    private Double estimatedCost;
    private Boolean isDeleted;
}
