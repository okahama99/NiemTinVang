package com.ntv.ntvcons_backend.services.fileStorage;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.ntv.ntvcons_backend.constants.FileType;
import com.ntv.ntvcons_backend.constants.FirebaseResource;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileCreateDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    public Object upload(MultipartFile multipartFile) {
        try {
            /* get original file name */
            String fileName = multipartFile.getOriginalFilename();

            /* generated random string values for file name */
            fileName =
                    UUID.randomUUID().toString().concat(this.getExtension(fileName));

            /* convert multipartFile to File */
            File file = this.convertToFile(multipartFile, fileName);

            /* get uploaded file link */
            String TEMP_URL = this.uploadFile(file, fileName);

            /* delete the copy of uploaded file stored in the project folder */
            if (file.delete())
                System.out.println("Deleted");

            // Your customized response
            return new ExternalFileCreateDTO(file.getName(), TEMP_URL, FileType.BLUEPRINT_DOC);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public Object download(String fileName) throws IOException {
        // Set random string for destination file name
        String destFileName =
                UUID.randomUUID().toString().concat(this.getExtension(fileName));

        // Set destination file path
        String destFilePath = "D:\\testFirebase\\" + destFileName;

        Credentials credentials =
                GoogleCredentials.fromStream(
                        Files.newInputStream(Paths.get(FirebaseResource.PRIVATE_KEY)));

        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        Blob blob = storage.get(
                BlobId.of(FirebaseResource.BUCKET_NAME, fileName));

        blob.downloadTo(Paths.get(destFilePath));

        return ResponseEntity.ok("Successfully Downloaded!");
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
        return fileName.substring(
                fileName.lastIndexOf("."));
    }


}
