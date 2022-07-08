package com.ntv.ntvcons_backend.dtos.projectManager;

import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import com.ntv.ntvcons_backend.dtos.user.UserReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectManagerReadDTO extends BaseReadDTO {
    private Long projectManagerId;
    private Long projectId;
    private UserReadDTO manager;
    private LocalDateTime assignDate;
    private LocalDateTime removeDate;
}
