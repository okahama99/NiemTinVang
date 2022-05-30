package com.ntv.ntvcons_backend.services.projectManager;

import com.ntv.ntvcons_backend.repositories.ProjectManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectManagerServiceImpl implements ProjectManagerService {
    @Autowired
    private ProjectManagerRepository projectManagerRepository;
}
