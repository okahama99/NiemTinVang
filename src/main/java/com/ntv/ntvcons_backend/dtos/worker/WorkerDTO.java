package com.ntv.ntvcons_backend.dtos.worker;

import com.ntv.ntvcons_backend.dtos.location.LocationDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerDTO implements Serializable {
    private Integer workerId;
    private String fullName;
    private String citizenId;
    private String socialSecurityCode;
    private LocationDTO address;
    private Boolean isDeleted = false;
}