package com.ntv.ntvcons_backend.dtos.worker;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerReadDTO extends BaseReadDTO {
    private Long workerId;
    private String fullName;
    private String citizenId;
    private String gender;
    private Date birthday;
    private String birthPlace;
    private String socialSecurityCode;
    private LocationReadDTO address;

    @JsonInclude(JsonInclude.Include.NON_NULL) /* if null => no return JSON */
    private Boolean isAvailable;
}