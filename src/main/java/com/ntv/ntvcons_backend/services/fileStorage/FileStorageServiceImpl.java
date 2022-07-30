package com.ntv.ntvcons_backend.services.fileStorage;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.ntv.ntvcons_backend.constants.FirebaseResource;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileCreateDTO;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    public Object upload(MultipartFile multipartFile) {
        try {
            /* get original file name */
            String fileName = multipartFile.getOriginalFilename();

            /* generated random string values for file name */
            fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));

            /* convert multipartFile to File */
            File file = this.convertToFile(multipartFile, fileName);

            /* get uploaded file link */
            String TEMP_URL = this.uploadFile(file, fileName);

            /* delete the copy of uploaded file stored in the project folder */
            file.delete();

            // Your customized response
            return new ExternalFileCreateDTO(file.getName(), TEMP_URL, 2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public Object download(String fileName) throws IOException {
        // to set random string for destination file name
        String destFileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));

        // to set destination file path
        String destFilePath = "Z:\\New folder\\" + destFileName;

        ////////////////////////////////   Download  ////////////////////////////////////////////////////////////////////////
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("path of JSON with genarated private key"));

        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        Blob blob = storage.get(BlobId.of("your bucket name", fileName));
        blob.downloadTo(Paths.get(destFilePath));
        return sendResponse("200", "Successfully Downloaded!");
    }


    /** Upload a File to Firebase */
    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of(FirebaseResource.BUCKET_NAME, fileName);

        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();

        Credentials credentials = GoogleCredentials.fromStream(
                Files.newInputStream(Paths.get(FirebaseResource.PRIVATE_KEY)));

        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        return String.format(
                FirebaseResource.DOWNLOAD_URL,
                URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()));
    }


    /** Convert MultipartFile to File */
    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);

        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }

        return tempFile;
    }

    /** Get extension of an uploaded file */
    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }


}
