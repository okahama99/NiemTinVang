package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.services.firebase.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/firebase")
public class FirebaseController {
    @Autowired
    private FirebaseService firebaseService;

//    @PostMapping(value = "/profile/pic", consumes = "multipart/form-data")
//    public Object upload(@RequestPart("file") MultipartFile multipartFile) {
////        logger.info("HIT -/upload | File Name : {}", multipartFile.getOriginalFilename());
//        return firebaseService.uploadToFirebase(multipartFile);
//    }
//
//    @GetMapping("/profile/pic/{fileName}")
//    public Object download(@PathVariable String fileName) throws IOException {
////        logger.info("HIT -/download | File Name : {}", fileName);
//        return firebaseService.downloadFromFirebase(fileName);
//    }
}
