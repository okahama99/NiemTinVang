package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.services.projectManager.ProjectManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projectManager")
public class ProjectManagerController {
    @Autowired
    private ProjectManagerService projectManagerService;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */

    /* READ */

    /* UPDATE */
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/v1/updateProjectManager", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> updateProjectManager(@RequestParam long projectManagerId,
                                                  @RequestParam long userId){
        boolean result = projectManagerService.updateProjectManager(projectManagerId,userId);
        if(result)
        {
            return new ResponseEntity<>("Cập nhật thành công.",HttpStatus.OK);
        }
        return new ResponseEntity<>("Cập nhật thất bại.",HttpStatus.BAD_REQUEST);
    }

    /* DELETE */

    /* ================================================ Ver 1 ================================================ */
}
