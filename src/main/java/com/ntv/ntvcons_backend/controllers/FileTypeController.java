package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.entities.FileType;
import com.ntv.ntvcons_backend.entities.fileTypeModels.FileTypeModel;
import com.ntv.ntvcons_backend.services.fileType.FileTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/FileType")
public class FileTypeController {
    @Autowired
    FileTypeService fileTypeService;

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/createFileType", produces = "application/json;charset=UTF-8")
    public HttpStatus createFileType(@RequestBody String fileTypeName,
                                     @RequestBody String fileTypeDesc,
                                     @RequestBody String fileTypeExtension){

        FileType result = fileTypeService.createFileType(fileTypeName, fileTypeDesc, fileTypeExtension);
        if(result!=null){
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/updateFileType", produces = "application/json;charset=UTF-8")
    public HttpStatus updateFileType(@RequestBody FileTypeModel fileTypeModel){
        boolean result = fileTypeService.updateFileType(fileTypeModel);
        if(result){
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/getAll", produces = "application/json;charset=UTF-8")
    public @ResponseBody List<FileType> getAll() {
        List<FileType> fileTypes = fileTypeService.getAll();
        return fileTypes;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/deleteFileType/{fileTypeId}", produces = "application/json;charset=UTF-8")
    public HttpStatus deleteFileType(@PathVariable(name = "fileTypeId") int fileTypeId){
        if(fileTypeService.deleteFileType(fileTypeId))
        {
            return HttpStatus.OK;
        }else{
            return HttpStatus.BAD_REQUEST;
        }
    }
}
