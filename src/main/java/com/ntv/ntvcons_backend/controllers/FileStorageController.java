package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.services.fileStorage.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/fileStorage")
public class FileStorageController {
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(value = "/profile/pic", consumes = "multipart/form-data")
    public Object upload(@RequestPart("file") MultipartFile multipartFile) {
//        logger.info("HIT -/upload | File Name : {}", multipartFile.getOriginalFilename());
        return fileStorageService.upload(multipartFile);
    }

    @GetMapping("/profile/pic/{fileName}")
    public Object download(@PathVariable String fileName) throws IOException {
//        logger.info("HIT -/download | File Name : {}", fileName);
        return fileStorageService.download(fileName);
    }
}
