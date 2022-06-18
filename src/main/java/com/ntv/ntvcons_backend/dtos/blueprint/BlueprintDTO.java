package com.ntv.ntvcons_backend.dtos.blueprint;

import com.ntv.ntvcons_backend.dtos.user.UserReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlueprintDTO implements Serializable {
    private Integer blueprintId;
    private String blueprintName;
    private UserReadDTO designer;
    private Double estimatedCost;
    private Boolean isDeleted = false;
}
