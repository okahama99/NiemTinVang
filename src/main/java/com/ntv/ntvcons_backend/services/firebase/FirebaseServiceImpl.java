package com.ntv.ntvcons_backend.services.firebase;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.ntv.ntvcons_backend.constants.FileType;
import com.ntv.ntvcons_backend.constants.FirebaseResource;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileCreateDTO;
import com.ntv.ntvcons_backend.services.externalFile.ExternalFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FirebaseServiceImpl implements FirebaseService {
    @Lazy
    @Autowired
    private ExternalFileService externalFileService;

    /* CREATE */
    /** Luồng đi: <br/>
     * 1. Tạo fileName = UUID cho random <br/>
     * 2. Convert MultipartFile (api input) => File (lưu 1 file local trong project) <br/>
     * 3. Up lên Firebase & lấy link URL về <br/>
     * 4. Xóa file lưu local trong project <br/>*/
    @Override
    public ExternalFileCreateDTO uploadToFirebase(MultipartFile multipartFile) throws Exception {
        /* get original fileName */
        String fileName = multipartFile.getOriginalFilename();

        if(fileName == null)
            throw new IllegalArgumentException("Filename can not be null");

        String fileExtension = getExtension(fileName);

        do { /* generated random fileName */
            fileName = UUID.randomUUID().toString().concat(fileExtension);
        } while(externalFileService /* Test duplicate */
                .existsByFileNameOrFileLink(fileName, makeLink(fileName)));

        /* convert multipartFile to File (Create a File in current project folder) */
        File file = convertMultipartFileToFile(multipartFile, fileName);

        /* get uploaded fileLink (upload to Firebase) */
        String fileLink = uploadFileToGetUrl(fileName, file);

        /* delete the File stored in current project folder */
        boolean localFileDeleted;
        int deleteTry = 0;
        do { /* Xóa cho bằng được */
            localFileDeleted = file.delete();
            deleteTry++;

            if (deleteTry >= 10) {
                /* Log error xóa hoài ko được, cần xóa manual */
                break;
            }
        } while (!localFileDeleted);

        ExternalFileCreateDTO fileCreateDTO = new ExternalFileCreateDTO();
        fileCreateDTO.setFileName(file.getName());
        fileCreateDTO.setFileLink(fileLink);

        return fileCreateDTO;
    }

    @Override
    public List<ExternalFileCreateDTO> uploadAllToFirebase(Collection<MultipartFile> multipartFileCollection) throws Exception {
        Map<String, MultipartFile> fileNameMultipartFileMap = new HashMap<>();

        String tmpFileName;
        for (MultipartFile multipartFile : multipartFileCollection) {
            /* get original fileName */
            tmpFileName = multipartFile.getOriginalFilename();

            if(tmpFileName == null)
                throw new IllegalArgumentException("Filename can not be null");

            do { /* generated random fileName */
                tmpFileName = UUID.randomUUID().toString().concat(getExtension(tmpFileName));
            } while(externalFileService /* Test duplicate */
                    .existsByFileNameOrFileLink(tmpFileName, makeLink(tmpFileName))
                    || fileNameMultipartFileMap.containsKey(tmpFileName));

            fileNameMultipartFileMap.put(tmpFileName, multipartFile);
        }

        Set<String> fileNameSet = fileNameMultipartFileMap.keySet();

        /* convert all multipartFile to File (Create a File in current project folder) */
        Map<String, File> fileNameFileMap = new HashMap<>();
        for (String fileName : fileNameSet) {
            fileNameFileMap.put(
                    fileName,
                    convertMultipartFileToFile(
                            fileNameMultipartFileMap.get(fileName), fileName));
        }

        /* get all uploaded fileLink (upload to Firebase) */
        Map<String, String> fileNameFileLinkMap =
                uploadAllFileToGetNameUrlMap(fileNameFileMap);

        List<ExternalFileCreateDTO> fileCreateDTOList = new ArrayList<>();

        /* delete the File stored in current project folder */
        boolean localFileDeleted;
        int deleteTry;
        for (String fileName : fileNameSet) {
            deleteTry = 0;
            do { /* Xóa cho bằng được */
                localFileDeleted =
                        fileNameFileMap.get(fileName).delete();
                deleteTry++;

                if (deleteTry >= 10) {
                    /* Log error */
                    break;
                }
            } while (!localFileDeleted);

            ExternalFileCreateDTO fileCreateDTO = new ExternalFileCreateDTO();
            fileCreateDTO.setFileName(fileName);
            fileCreateDTO.setFileLink(fileNameFileLinkMap.get(fileName));

            fileCreateDTOList.add(fileCreateDTO);
        }

        return fileCreateDTOList;
    }

    /* READ */
    /* TODO: not yet required */
//    @Override
//    public File downloadFromFirebase(String fileName) throws Exception {
//        Credentials credentials =
//                GoogleCredentials.fromStream(
//                        Files.newInputStream(Paths.get(FirebaseResource.PRIVATE_KEY)));
//
//        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
//
//        Blob blob = storage.get(
//                BlobId.of(FirebaseResource.BUCKET_NAME, fileName));
//
//        File file = new File(blob.getName());
//
//        /* Create a File in current project folder */
//        try (FileOutputStream fos = new FileOutputStream(file)) {
//            fos.write(blob.getContent());
//        }
//
//        return file;
//    }

    /* UPDATE */

    /* DELETE */
    @Override
    public boolean deleteFromFirebase(String fileName) throws Exception {
        /* Get auth key save in local JSON file */
        Credentials credentials =
                GoogleCredentials.fromStream(
                        Files.newInputStream(Paths.get(FirebaseResource.PRIVATE_KEY)));

        /* Get Firebase Storage connection */
        Storage storage =
                StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        /* Get Blob (File) by name in Firebase storage */
        Blob blob = storage.get(
                BlobId.of(FirebaseResource.BUCKET_NAME, fileName));

        if (blob == null)
            return false; /* Not found with name */

        return blob.delete();
    }

    @Override
    public boolean deleteAllFromFirebase(Collection<String> fileNameCollection) throws Exception {
        /* Get auth key save in local JSON file */
        Credentials credentials =
                GoogleCredentials.fromStream(
                        Files.newInputStream(Paths.get(FirebaseResource.PRIVATE_KEY)));

        /* Get Firebase Storage connection */
        Storage storage =
                StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        /* Make all BlobId (path) by fileName */
        List<BlobId> blobIdList = new ArrayList<>();
        for (String fileName : fileNameCollection) {
            blobIdList.add(
                    BlobId.of(FirebaseResource.BUCKET_NAME, fileName));
        }

        /* Delete all Blob (File) by blobIdList (pathList) in Firebase storage */
        List<Boolean> deleteResultList = storage.delete(blobIdList);

        /* return true if no error/false deletion */
        return !deleteResultList.contains(false);
    }

    /* Utils */
    private String uploadFileToGetUrl(String fileName, File file) throws Exception {
        /* Blob: An object in Google Cloud Storage */
        BlobId blobId = BlobId.of(FirebaseResource.BUCKET_NAME, fileName);

        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();

        /* Get auth key save in local JSON file */
        Credentials credentials =
                GoogleCredentials.fromStream(
                        Files.newInputStream(Paths.get(FirebaseResource.PRIVATE_KEY)));

        /* Get Firebase Storage connection */
        Storage storage =
                StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        /* Create file on Firebase storage */
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        /* Return url */
        return makeLink(fileName);
    }

    private Map<String, String> uploadAllFileToGetNameUrlMap(Map<String, File> fileNameFileMap) throws Exception {
        /* Get auth key save in local JSON file */
        Credentials credentials =
                GoogleCredentials.fromStream(
                        Files.newInputStream(Paths.get(FirebaseResource.PRIVATE_KEY)));

        /* Get Firebase Storage connection */
        Storage storage =
                StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        Map<String, String> fileNameFileLinkMap = new HashMap<>();

        for (String fileName : fileNameFileMap.keySet()) {
            BlobId blobId = BlobId.of(FirebaseResource.BUCKET_NAME, fileName);

            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();

            /* Create file on Firebase storage */
            storage.create(blobInfo, Files.readAllBytes(fileNameFileMap.get(fileName).toPath()));

            fileNameFileLinkMap.put(fileName, makeLink(fileName));
        }

        /* Return nameUrlMap */
        return fileNameFileLinkMap;
    }

    private String makeLink(String fileName) throws Exception {
        return String.format(
                FirebaseResource.DOWNLOAD_URL,
                URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()));
    }

    private File convertMultipartFileToFile(MultipartFile multipartFile, String fileName) throws Exception {
        File file = new File(fileName);

        /* Create a File in current project folder */
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }

        return file;
    }

    private String getExtension(String fileName) throws Exception {
        int lastDotIndex = fileName.lastIndexOf(".");

        /* If file don't have extension, return empty String */
        if (lastDotIndex <= -1)
            return "";

        return fileName.substring(lastDotIndex);
    }
}
