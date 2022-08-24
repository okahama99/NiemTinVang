package com.ntv.ntvcons_backend.services.misc;

import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.constants.FileType;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileCreateDTO;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileReadDTO;
import com.ntv.ntvcons_backend.services.externalFile.ExternalFileService;
import com.ntv.ntvcons_backend.services.externalFileEntityWrapperPairing.ExternalFileEntityWrapperPairingService;
import com.ntv.ntvcons_backend.services.firebase.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FileCombineServiceImpl implements FileCombineService {
    @Lazy
    @Autowired
    private ExternalFileService externalFileService;
    @Lazy
    @Autowired
    private ExternalFileEntityWrapperPairingService eFEWPairingService;
    @Lazy
    @Autowired
    private FirebaseService firebaseService;

    /* READ */
    public boolean saveFileInDBAndFirebase(
            MultipartFile multipartFile, FileType fileType, long entityId, EntityType entityType, Long createdBy) throws Exception {
        /* Save File to Firebase (get name & link) */
        ExternalFileCreateDTO fileCreateDTO =
                firebaseService.uploadToFirebase(multipartFile);
        fileCreateDTO.setFileType(fileType);
        fileCreateDTO.setCreatedBy(createdBy);

        /* Save fileName & fileLink to DB */
        ExternalFileReadDTO newFileDTO =
                externalFileService.createExternalFileByDTO(fileCreateDTO);

        /* Create pairing */
        eFEWPairingService
                .createPairing(entityId, entityType, newFileDTO.getFileId(), createdBy);

        return true;
    }

    public boolean saveAllFileInDBAndFirebase(
            List<MultipartFile> multipartFileList, FileType fileType, long entityId, EntityType entityType, Long createdBy) throws Exception {
        List<ExternalFileCreateDTO> fileCreateDTOList =
                firebaseService.uploadAllToFirebase(multipartFileList);
        fileCreateDTOList = fileCreateDTOList.stream()
                .peek(externalFileCreateDTO -> {
                    externalFileCreateDTO.setFileType(fileType);
                    externalFileCreateDTO.setCreatedBy(createdBy);})
                .collect(Collectors.toList());

        /* Save fileName & fileLink to DB */
        List<ExternalFileReadDTO> newFileDTOList =
                externalFileService.createBulkExternalFileByDTO(fileCreateDTOList);

        Set<Long> fileIdSet =
                newFileDTOList.stream()
                        .map(ExternalFileReadDTO::getFileId)
                        .collect(Collectors.toSet());

        /* Create pairing */
        eFEWPairingService
                .createBulkPairingByEntityIdAndEntityType(entityId, entityType, fileIdSet, createdBy);

        return true;
    }

    /* READ */

    /* UPDATE */

    /* DELETE */
    public boolean deleteFileInDBAndFirebaseByFileDTO(ExternalFileReadDTO fileDTO) throws Exception {
        boolean isDBDeletedSuccess =
                externalFileService.deleteExternalFile(fileDTO.getFileId());

        boolean isFirebaseDeletedSuccess =
                firebaseService.deleteFromFirebase(fileDTO.getFileName());

        boolean isPairingDeletedSuccess =
                eFEWPairingService.deleteAllByExternalFileId(fileDTO.getFileId());

        return (isDBDeletedSuccess && isFirebaseDeletedSuccess && isPairingDeletedSuccess);
    }

    public boolean deleteAllFileInDBAndFirebaseByFileDTO(Collection<ExternalFileReadDTO> fileDTOCollection) throws Exception {
        Set<Long> fileIdSet = new HashSet<>();
        Set<String> fileNameSet = new HashSet<>();

        for (ExternalFileReadDTO fileDTO : fileDTOCollection) {
            fileIdSet.add(fileDTO.getFileId());
            fileNameSet.add(fileDTO.getFileName());
        }

        boolean isDBDeletedSuccess =
                externalFileService.deleteAllByIdIn(fileIdSet);

        boolean isFirebaseDeletedSuccess =
                firebaseService.deleteAllFromFirebase(fileNameSet);

        boolean isPairingDeletedSuccess =
                eFEWPairingService.deleteAllByExternalFileIdIn(fileIdSet);

        return (isDBDeletedSuccess && isFirebaseDeletedSuccess && isPairingDeletedSuccess);
    }

}
