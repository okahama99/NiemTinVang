package com.ntv.ntvcons_backend.utils;

import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.constants.FileType;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileCreateDTO;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileReadDTO;
import com.ntv.ntvcons_backend.services.externalFile.ExternalFileService;
import com.ntv.ntvcons_backend.services.externalFileEntityWrapperPairing.ExternalFileEntityWrapperPairingService;
import com.ntv.ntvcons_backend.services.firebase.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MiscUtil {
    /** Small Util to return {@link Pageable} to replace dup code in serviceImpl */
    public Pageable makePaging(int pageNo, int pageSize, String sortBy, boolean sortTypeAsc) {
        if (sortTypeAsc) {
            return PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            return PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }
    }

    public String getExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");

        /* If file don't have extension, return empty String */
        if (lastDotIndex <= -1)
            return "";

        return fileName.substring(lastDotIndex);
    }

    public File convertMultipartFileToFile(MultipartFile multipartFile, String fileName) throws Exception {
        File file = new File(fileName);

        /* Create a File in current project folder */
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }

        return file;
    }

}
