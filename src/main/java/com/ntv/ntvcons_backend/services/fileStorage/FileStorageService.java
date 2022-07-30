package com.ntv.ntvcons_backend.services.fileStorage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    Object upload(MultipartFile multipartFile);

    Object download(String fileName) throws IOException;
}
