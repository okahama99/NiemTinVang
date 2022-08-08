package com.ntv.ntvcons_backend.services.firebase;

import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileCreateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

public interface FirebaseService {
    /* CREATE */
    ExternalFileCreateDTO uploadToFirebase(MultipartFile multipartFile) throws Exception;

    List<ExternalFileCreateDTO> uploadAllToFirebase(Collection<MultipartFile> multipartFileCollection) throws Exception;

    /* READ */
//    TODO: not yet required
//    File downloadFromFirebase(String fileName) throws Exception;

    /* UPDATE */

    /* DELETE */
    boolean deleteFromFirebase(String fileName) throws Exception;

    boolean deleteAllFromFirebase(Collection<String> fileNameCollection) throws Exception;
}
