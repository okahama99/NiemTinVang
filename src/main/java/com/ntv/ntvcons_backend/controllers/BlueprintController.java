package com.ntv.ntvcons_backend.controllers;


import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.entities.BlueprintModels.CreateBlueprintModel;
import com.ntv.ntvcons_backend.entities.BlueprintModels.ShowBlueprintModel;
import com.ntv.ntvcons_backend.entities.BlueprintModels.UpdateBlueprintModel;
import com.ntv.ntvcons_backend.services.blueprint.BlueprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blueprint")
public class BlueprintController {
    @Autowired
    private BlueprintService blueprintService;

    /* ================================================ Ver 1 ================================================ */
    /* CREATE */
    @PreAuthorize("hasAnyRole('Admin','Staff')")
    @PostMapping(value = "/v1/createBlueprint", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createBlueprint(@RequestBody CreateBlueprintModel blueprint) {
        /* TODO: create blueprint */
        return null;
    }

    /* READ */
    @PreAuthorize("hasAnyRole('Admin','Staff','Customer')")
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortType) {
        try {
            List<ShowBlueprintModel> blueprints = blueprintService.getAll(pageNo, pageSize, sortBy, sortType);

            if (blueprints == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Blueprint found");
            }

            return ResponseEntity.ok().body(blueprints);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for Blueprint", e.getMessage()));
        }
    }


    @GetMapping(value = "/v1/checkDuplicate", produces = "application/json;charset=UTF-8")
    public @ResponseBody String checkDuplicate(@RequestParam String blueprintName) {
        String result = blueprintService.checkDuplicate(blueprintName);
        return result;
    }

    /* UPDATE */
    @PreAuthorize("hasAnyRole('Admin','Staff')")
    @PutMapping(value = "/v1/updateBlueprint", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updateBlueprint(@RequestBody UpdateBlueprintModel blueprint) {
        /* TODO: update blueprint */
        return null;
    }

    /* DELETE */
    @PreAuthorize("hasAnyRole('Admin')")
    @DeleteMapping(value = "/v1/deleteBlueprint/{blueprintId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deleteBlueprint(@PathVariable(name = "blueprintId") long blueprintId) {
        try {
            if (!blueprintService.deleteBlueprint(blueprintId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Blueprint found with Id: '" + blueprintId + "'. ");
            }

            return ResponseEntity.ok().body("Deleted Blueprint with Id: '" + blueprintId + "'. ");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting Blueprint with Id: '" + blueprintId + "'. ", e.getMessage()));
        }
    }
    /* ================================================ Ver 1 ================================================ */

}

