package com.ntv.ntvcons_backend.services.fileStorage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileStorageService {
    Object upload(MultipartFile multipartFile);

    Object download(String fileName) throws IOException;
}
