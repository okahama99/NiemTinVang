package com.ntv.ntvcons_backend.controllers;


import com.ntv.ntvcons_backend.entities.BlueprintModels.ShowBlueprintModel;
import com.ntv.ntvcons_backend.services.blueprint.BlueprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Blueprint")
public class BlueprintController {
    @Autowired
    BlueprintService blueprintService;

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/getAllBlueprint", produces = "application/json;charset=UTF-8")
    public @ResponseBody List<ShowBlueprintModel> getAllBlueprint(@RequestParam int pageNo,
                                                         @RequestParam int pageSize,
                                                         @RequestParam String sortBy,
                                                         @RequestParam boolean sortType) {
        List<ShowBlueprintModel> projects = blueprintService.getAll(pageNo, pageSize, sortBy, sortType);
        return projects;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/deleteBlueprint/{blueprintId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> deleteProjectBlueprint(@PathVariable(name = "blueprintId") int projectBlueprintId) {
        if(blueprintService.deleteProjectBlueprint(projectBlueprintId))
        {
            return new ResponseEntity<>("Xóa thành công.",HttpStatus.OK);
        }
        return new ResponseEntity<>("Xóa thất bại.",HttpStatus.BAD_REQUEST);
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/checkDuplicate", produces = "application/json;charset=UTF-8")
    public @ResponseBody String checkDuplicate(@RequestParam String blueprintName) {
        String result = blueprintService.checkDuplicate(blueprintName);
        return result;
    }
}

