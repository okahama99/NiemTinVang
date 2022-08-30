package com.ntv.ntvcons_backend.services.firebase;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.ntv.ntvcons_backend.constants.FirebaseResource;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileCreateDTO;
import com.ntv.ntvcons_backend.services.externalFile.ExternalFileService;
import com.ntv.ntvcons_backend.utils.MiscUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class FirebaseServiceImpl implements FirebaseService {
    @Lazy
    @Autowired
    private ExternalFileService externalFileService;
    @Autowired
    private MiscUtil miscUtil;

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

        String fileExtension = miscUtil.getExtension(fileName);

        String fileNameFirebase = "";
        do { /* generated random fileName */
            fileNameFirebase = UUID.randomUUID().toString().concat(fileExtension);
        } while(externalFileService /* Test duplicate */
                .existsByFileNameFirebaseOrFileLink(fileNameFirebase, makeFirebaseLink(fileNameFirebase)));

        /* convert multipartFile to File (Create a File in current project folder) */
        File file = miscUtil.convertMultipartFileToFile(multipartFile, fileNameFirebase);

        /* get uploaded fileLink (upload to Firebase) */
        String fileLink = uploadFileToGetUrl(fileNameFirebase, file);

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
        fileCreateDTO.setFileName(fileName);
        fileCreateDTO.setFileNameFirebase(fileNameFirebase);
        fileCreateDTO.setFileLink(fileLink);

        return fileCreateDTO;
    }

    @Override
    public List<ExternalFileCreateDTO> uploadAllToFirebase(Collection<MultipartFile> multipartFileCollection) throws Exception {
        Map<String, MultipartFile> fileNameFirebaseMultipartFileMap = new HashMap<>();
        Map<String, String> fileNameFirebaseFileNameMap = new HashMap<>();

        String tmpFileName;
        String tmpFileNameFirebase;
        for (MultipartFile multipartFile : multipartFileCollection) {
            /* get original fileName */
            tmpFileName = multipartFile.getOriginalFilename();

            if(tmpFileName == null)
                throw new IllegalArgumentException("Filename can not be null");

            do { /* generated random fileName */
                tmpFileNameFirebase = UUID.randomUUID().toString().concat(miscUtil.getExtension(tmpFileName));
            } while(externalFileService /* Test duplicate */
                    .existsByFileNameFirebaseOrFileLink(tmpFileNameFirebase, makeFirebaseLink(tmpFileNameFirebase))
                    || fileNameFirebaseMultipartFileMap.containsKey(tmpFileNameFirebase));

            fileNameFirebaseMultipartFileMap.put(tmpFileNameFirebase, multipartFile);
            fileNameFirebaseFileNameMap.put(tmpFileNameFirebase, tmpFileName);
        }

        Set<String> fileNameSet = fileNameFirebaseMultipartFileMap.keySet();

        /* convert all multipartFile to File (Create a File in current project folder) */
        Map<String, File> fileNameFirebaseFileMap = new HashMap<>();
        for (String fileNameFirebase : fileNameSet) {
            fileNameFirebaseFileMap.put(
                    fileNameFirebase,
                    miscUtil.convertMultipartFileToFile(
                            fileNameFirebaseMultipartFileMap.get(fileNameFirebase), fileNameFirebase));
        }

        /* get all uploaded fileLink (upload to Firebase) */
        Map<String, String> fileNameFirebaseFileLinkMap =
                uploadAllFileToGetNameUrlMap(fileNameFirebaseFileMap);

        List<ExternalFileCreateDTO> fileCreateDTOList = new ArrayList<>();

        /* delete the File stored in current project folder */
        boolean localFileDeleted;
        int deleteTry;
        for (String fileNameFirebase : fileNameSet) {
            deleteTry = 0;
            do { /* Xóa cho bằng được */
                localFileDeleted =
                        fileNameFirebaseFileMap.get(fileNameFirebase).delete();
                deleteTry++;

                if (deleteTry >= 10) {
                    /* Log error */
                    break;
                }
            } while (!localFileDeleted);

            ExternalFileCreateDTO fileCreateDTO = new ExternalFileCreateDTO();
            fileCreateDTO.setFileName(fileNameFirebaseFileNameMap.get(fileNameFirebase));
            fileCreateDTO.setFileNameFirebase(fileNameFirebase);
            fileCreateDTO.setFileLink(fileNameFirebaseFileLinkMap.get(fileNameFirebase));

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
    public boolean deleteFromFirebase(String fileNameFirebase) throws Exception {
        /* Get auth key save in local JSON file */
        Credentials credentials =
                GoogleCredentials.fromStream(
                        Files.newInputStream(Paths.get(FirebaseResource.PRIVATE_KEY)));

        /* Get Firebase Storage connection */
        Storage storage =
                StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        /* Get Blob (File) by name in Firebase storage */
        Blob blob = storage.get(
                BlobId.of(FirebaseResource.BUCKET_NAME, fileNameFirebase));

        if (blob == null)
            return false; /* Not found with name */

        return blob.delete();
    }

    @Override
    public boolean deleteAllFromFirebase(Collection<String> fileNameFirebaseCollection) throws Exception {
        /* Get auth key save in local JSON file */
        Credentials credentials =
                GoogleCredentials.fromStream(
                        Files.newInputStream(Paths.get(FirebaseResource.PRIVATE_KEY)));

        /* Get Firebase Storage connection */
        Storage storage =
                StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        /* Make all BlobId (path) by fileName */
        List<BlobId> blobIdList = new ArrayList<>();
        for (String fileNameFirebase : fileNameFirebaseCollection) {
            blobIdList.add(
                    BlobId.of(FirebaseResource.BUCKET_NAME, fileNameFirebase));
        }

        /* Delete all Blob (File) by blobIdList (pathList) in Firebase storage */
        List<Boolean> deleteResultList = storage.delete(blobIdList);

        /* return true if no error/false deletion */
        return !deleteResultList.contains(false);
    }

    /* Utils */
    private String uploadFileToGetUrl(String fileNameFirebase, File file) throws Exception {
        /* Blob: An object in Google Cloud Storage */
        BlobId blobId = BlobId.of(FirebaseResource.BUCKET_NAME, fileNameFirebase);

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
        return makeFirebaseLink(fileNameFirebase);
    }

    private Map<String, String> uploadAllFileToGetNameUrlMap(Map<String, File> fileNameFirebaseFileMap) throws Exception {
        /* Get auth key save in local JSON file */
        Credentials credentials =
                GoogleCredentials.fromStream(
                        Files.newInputStream(Paths.get(FirebaseResource.PRIVATE_KEY)));

        /* Get Firebase Storage connection */
        Storage storage =
                StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        Map<String, String> fileNameFileLinkMap = new HashMap<>();

        for (String fileName : fileNameFirebaseFileMap.keySet()) {
            BlobId blobId = BlobId.of(FirebaseResource.BUCKET_NAME, fileName);

            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();

            /* Create file on Firebase storage */
            storage.create(blobInfo, Files.readAllBytes(fileNameFirebaseFileMap.get(fileName).toPath()));

            fileNameFileLinkMap.put(fileName, makeFirebaseLink(fileName));
        }

        /* Return nameUrlMap */
        return fileNameFileLinkMap;
    }

    private String makeFirebaseLink(String fileNameFirebase) throws Exception {
        return String.format(
                FirebaseResource.DOWNLOAD_URL,
                URLEncoder.encode(fileNameFirebase, StandardCharsets.UTF_8.toString()));
    }

}
