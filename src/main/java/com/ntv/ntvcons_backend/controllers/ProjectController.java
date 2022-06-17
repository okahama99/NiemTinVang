package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.entities.ProjectModels.ProjectModel;
import com.ntv.ntvcons_backend.entities.ProjectModels.CreateProjectModel;
import com.ntv.ntvcons_backend.services.project.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Project")
public class ProjectController {
    @Autowired
    ProjectService projectService;

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/createProject", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> createProject(@RequestBody CreateProjectModel createProjectModel){
        if(projectService.checkDuplicate(createProjectModel.getProjectName()))
        {
            return new ResponseEntity<>("Tên dự án đã tồn tại.",HttpStatus.BAD_REQUEST);
        }else{
            boolean result = projectService.createProject(createProjectModel);
            if(result)
            {
                return new ResponseEntity<>("Tạo thành công.",HttpStatus.OK);
            }
            return new ResponseEntity<>("Tạo thất bại.",HttpStatus.BAD_REQUEST);
        }
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/updateProject", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> updateProject(@RequestBody ProjectModel projectModel){
        boolean result = projectService.updateProject(projectModel);
        if(result)
        {
            return new ResponseEntity<>("Cập nhật thành công.",HttpStatus.OK);
        }
        return new ResponseEntity<>("Cập nhật thất bại.",HttpStatus.BAD_REQUEST);
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/getAllProject", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ProjectModel> getAllProject(@RequestParam int pageNo,
                              @RequestParam int pageSize,
                              @RequestParam String sortBy,
                              @RequestParam boolean sortType) {
        try {
            List<ProjectModel> projects = projectService.getAll(pageNo, pageSize, sortBy, sortType);
            return projects;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/getAllById", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ProjectModel> getAllById(   @RequestParam long projectId,
                                     @RequestParam int pageNo,
                                     @RequestParam int pageSize,
                                     @RequestParam String sortBy,
                                     @RequestParam boolean sortType) {
        List<ProjectModel> projects = projectService.getAllById(projectId, pageNo, pageSize, sortBy, sortType);
        return projects;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/deleteProject/{projectId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> deleteProject(@PathVariable(name = "projectId") int projectId){
        if(projectService.deleteProject(projectId))
        {
            return new ResponseEntity<>("Xóa thành công.",HttpStatus.OK);
        }
        return new ResponseEntity<>("Xóa thất bại.",HttpStatus.BAD_REQUEST);
    }


}
