package com.ntv.ntvcons_backend.services.projectBlueprint;

import com.ntv.ntvcons_backend.repositories.ProjectBlueprintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectBlueprintServiceImpl implements ProjectBlueprintService {
    @Autowired
    private ProjectBlueprintRepository projectBlueprintRepository;
}
