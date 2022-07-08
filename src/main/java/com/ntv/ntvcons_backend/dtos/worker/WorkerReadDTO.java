package com.ntv.ntvcons_backend.dtos.worker;

import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import com.ntv.ntvcons_backend.dtos.location.LocationReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerReadDTO extends BaseReadDTO {
    private Long workerId;
    private String fullName;
    private String citizenId;
    private String socialSecurityCode;
    private LocationReadDTO address;
}