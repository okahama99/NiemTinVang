package com.ntv.ntvcons_backend.dtos.projectWorker;

import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import com.ntv.ntvcons_backend.dtos.worker.WorkerReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectWorkerReadDTO extends BaseReadDTO {
    private Long projectWorkerId;
    private Long projectId;
    private WorkerReadDTO worker;
    private LocalDateTime assignDate;
    private LocalDateTime removeDate;
}
