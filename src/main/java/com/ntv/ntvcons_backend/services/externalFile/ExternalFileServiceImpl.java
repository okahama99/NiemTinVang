package com.ntv.ntvcons_backend.services.externalFile;

import com.ntv.ntvcons_backend.constants.FileType;
import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileCreateDTO;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileReadDTO;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileUpdateDTO;
import com.ntv.ntvcons_backend.entities.ExternalFile;
import com.ntv.ntvcons_backend.repositories.ExternalFileRepository;
import com.ntv.ntvcons_backend.services.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ExternalFileServiceImpl implements ExternalFileService {
    @Autowired
    private ExternalFileRepository externalFileRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private UserService userService;

    private final List<Status> N_D_S_STATUS_LIST = Status.getAllNonDefaultSearchStatus();

    /* CREATE */
    @Override
    public ExternalFile createExternalFile(ExternalFile newFile) throws Exception {
        String errorMsg = "";

        /* Check valid input */
        try {
            /* Check protocol */
            URL url = new URL(newFile.getFileLink());

            /* Check connection */
            URLConnection conn = url.openConnection();
            conn.connect();
        } catch (MalformedURLException e) {
            /* URL is not in a valid form */
            errorMsg += "Invalid link: '" + newFile.getFileLink() + "'. ";
        } catch (IOException e) {
            /* Connection couldn't be established */
            errorMsg += "Could not connect to link: '" + newFile.getFileLink() + "'. ";
        }

        /* Check FK */
        if (userService.existsById(newFile.getCreatedBy())) {
            errorMsg += "No User (CreatedBy) found with Id: '" + newFile.getCreatedBy()
                    + "'. Which violate constraint: FK_ExternalFile_User_CreatedBy. ";
        }

        /* Check duplicate */
        if (externalFileRepository
                .existsByFileNameOrFileLinkAndStatusNotIn(
                        newFile.getFileName(),
                        newFile.getFileLink(),
                        N_D_S_STATUS_LIST)) {
            errorMsg += "Already exists another ExternalFile with name: '" + newFile.getFileName()
                    + "', or with link: '" + newFile.getFileLink() + "'. ";
        }

        if (!errorMsg.trim().isEmpty())
            throw new IllegalArgumentException(errorMsg);

        return externalFileRepository.saveAndFlush(newFile);
    }
    @Override
    public ExternalFileReadDTO createExternalFileByDTO(ExternalFileCreateDTO newFileDTO) throws Exception {
        ExternalFile newFile = modelMapper.map(newFileDTO, ExternalFile.class);

        newFile = createExternalFile(newFile);

        return fillDTO(newFile);
    }

    /* READ */
    @Override
    public Page<ExternalFile> getPageAll(Pageable paging) throws Exception {
        Page<ExternalFile> filePage =
                externalFileRepository.findAllByStatusNotIn(N_D_S_STATUS_LIST, paging);

        if (filePage.isEmpty())
            return null;

        return filePage;
    }
    @Override
    public List<ExternalFileReadDTO> getAllDTOInPaging(Pageable paging) throws Exception {
        Page<ExternalFile> filePage = getPageAll(paging);

        if (filePage == null)
            return null;

        List<ExternalFile> fileList = filePage.getContent();

        if (fileList.isEmpty())
            return null;

        return fillAllDTO(fileList, filePage.getTotalPages());
    }

    @Override
    public boolean existsById(long fileId) throws Exception {
        return externalFileRepository
                .existsByFileIdAndStatusNotIn(fileId, N_D_S_STATUS_LIST);
    }
    @Override
    public ExternalFile getById(long fileId) throws Exception {
        return externalFileRepository
                .findByFileIdAndStatusNotIn(fileId, N_D_S_STATUS_LIST)
                .orElse(null);
    }
    @Override
    public ExternalFileReadDTO getDTOById(long fileId) throws Exception {
        ExternalFile file = getById(fileId);

        if (file == null)
            return null;

        return fillDTO(file);
    }

    @Override
    public boolean existsAllByIdIn(Collection<Long> fileIdCollection) throws Exception {
        return externalFileRepository
                .existsAllByFileIdInAndStatusNotIn(fileIdCollection, N_D_S_STATUS_LIST);
    }
    @Override
    public List<ExternalFile> getAllByIdIn(Collection<Long> fileIdCollection) throws Exception {
        List<ExternalFile> fileList =
                externalFileRepository.findAllByFileIdInAndStatusNotIn(fileIdCollection, N_D_S_STATUS_LIST);

        if (fileList.isEmpty())
            return null;

        return fileList;
    }
    @Override
    public List<ExternalFileReadDTO> getAllDTOByIdIn(Collection<Long> fileIdCollection) throws Exception {
        List<ExternalFile> fileList = getAllByIdIn(fileIdCollection);

        if (fileList == null)
            return null;

        return fillAllDTO(fileList, null);
    }
    @Override
    public Map<Long, ExternalFileReadDTO> mapFileIdExternalFileDTOByIdIn(Collection<Long> fileIdCollection) throws Exception {
        List<ExternalFileReadDTO> fileDTOList = getAllDTOByIdIn(fileIdCollection);

        if (fileDTOList == null)
            return new HashMap<>();

        return fileDTOList.stream()
                .collect(Collectors.toMap(ExternalFileReadDTO::getFileId, Function.identity()));
    }

    @Override
    public List<ExternalFile> getAllByFileType(FileType fileType) throws Exception {
        List<ExternalFile> fileList =
                externalFileRepository.findAllByFileTypeAndStatusNotIn(fileType, N_D_S_STATUS_LIST);

        if (fileList.isEmpty())
            return null;

        return fileList;
    }
    @Override
    public List<ExternalFileReadDTO> getAllDTOByFileType(FileType fileType) throws Exception {
        List<ExternalFile> fileList = getAllByFileType(fileType);

        if (fileList == null)
            return null;

        return fillAllDTO(fileList, null);
    }
    @Override
    public Page<ExternalFile> getPageAllByFileType(Pageable paging, FileType fileType) throws Exception {
        Page<ExternalFile> filePage =
                externalFileRepository.findAllByFileTypeAndStatusNotIn(fileType, N_D_S_STATUS_LIST, paging);

        if (filePage.isEmpty())
            return null;

        return filePage;
    }
    @Override
    public List<ExternalFileReadDTO> getAllDTOInPagingByFileType(Pageable paging, FileType fileType) throws Exception {
        Page<ExternalFile> filePage = getPageAllByFileType(paging, fileType);

        if (filePage == null)
            return null;

        List<ExternalFile> fileList = filePage.getContent();

        if (fileList.isEmpty())
            return null;

        return fillAllDTO(fileList, filePage.getTotalPages());
    }

    @Override
    public ExternalFile getByFileName(String fileName) throws Exception {
        return externalFileRepository
                .findByFileNameAndStatusNotIn(fileName, N_D_S_STATUS_LIST)
                .orElse(null);
    }
    @Override
    public ExternalFileReadDTO getDTOByFileName(String fileName) throws Exception {
        ExternalFile file = getByFileName(fileName);

        if (file == null)
            return null;

        return fillDTO(file);
    }

    @Override
    public List<ExternalFile> getAllByFileNameContains(String fileName) throws Exception {
        List<ExternalFile> fileList =
                externalFileRepository.findAllByFileNameContainsAndStatusNotIn(fileName, N_D_S_STATUS_LIST);

        if (fileList.isEmpty())
            return null;

        return fileList;
    }
    @Override
    public List<ExternalFileReadDTO> getAllDTOByFileNameContains(String fileName) throws Exception {
        List<ExternalFile> fileList = getAllByFileNameContains(fileName);

        if (fileList == null)
            return null;

        return fillAllDTO(fileList, null);
    }
    @Override
    public Page<ExternalFile> getPageAllByFileNameContains(Pageable paging, String fileName) throws Exception {
        Page<ExternalFile> filePage =
                externalFileRepository.findAllByFileNameContainsAndStatusNotIn(fileName, N_D_S_STATUS_LIST, paging);

        if (filePage.isEmpty())
            return null;

        return filePage;
    }
    @Override
    public List<ExternalFileReadDTO> getAllDTOInPagingByFileNameContains(Pageable paging, String fileName) throws Exception {
        Page<ExternalFile> filePage = getPageAllByFileNameContains(paging, fileName);

        if (filePage == null)
            return null;

        List<ExternalFile> fileList = filePage.getContent();

        if (fileList.isEmpty())
            return null;

        return fillAllDTO(fileList, filePage.getTotalPages());
    }

    @Override
    public ExternalFile getByFileLink(String fileLink) throws Exception {
        return externalFileRepository
                .findByFileLinkAndStatusNotIn(fileLink, N_D_S_STATUS_LIST)
                .orElse(null);
    }
    @Override
    public ExternalFileReadDTO getDTOByFileLink(String fileLink) throws Exception {
        ExternalFile file = getByFileLink(fileLink);

        if (file == null)
            return null;

        return fillDTO(file);
    }

    @Override
    public List<ExternalFile> getAllByFileLinkContains(String fileLink) throws Exception {
        List<ExternalFile> fileList =
                externalFileRepository.findAllByFileLinkContainsAndStatusNotIn(fileLink, N_D_S_STATUS_LIST);

        if (fileList.isEmpty())
            return null;

        return fileList;
    }
    @Override
    public List<ExternalFileReadDTO> getAllDTOByFileLinkContains(String fileLink) throws Exception {
        List<ExternalFile> fileList = getAllByFileLinkContains(fileLink);

        if (fileList == null)
            return null;

        return fillAllDTO(fileList, null);
    }
    @Override
    public Page<ExternalFile> getPageAllByFileLinkContains(Pageable paging, String fileLink) throws Exception {
        Page<ExternalFile> filePage =
                externalFileRepository.findAllByFileLinkContainsAndStatusNotIn(fileLink, N_D_S_STATUS_LIST, paging);

        if (filePage.isEmpty())
            return null;

        return filePage;
    }
    @Override
    public List<ExternalFileReadDTO> getAllDTOInPagingByFileLinkContains(Pageable paging, String fileLink) throws Exception {
        Page<ExternalFile> filePage = getPageAllByFileLinkContains(paging, fileLink);

        if (filePage == null)
            return null;

        List<ExternalFile> fileList = filePage.getContent();

        if (fileList.isEmpty())
            return null;

        return fillAllDTO(fileList, filePage.getTotalPages());
    }

    /* UPDATE */
    @Override
    public ExternalFile updateExternalFile(ExternalFile updatedFile) throws Exception {
        ExternalFile oldFile = getById(updatedFile.getFileId());

        if (oldFile == null)
            return null;

        String errorMsg = "";

        /* Check valid input */
        try {
            /* Check protocol */
            URL url = new URL(updatedFile.getFileLink());

            /* Check connection */
            URLConnection conn = url.openConnection();
            conn.connect();
        } catch (MalformedURLException e) {
            /* URL is not in a valid form */
            errorMsg += "Invalid link: '" + updatedFile.getFileLink() + "'. ";
        } catch (IOException e) {
            /* Connection couldn't be established */
            errorMsg += "Could not connect to link: '" + updatedFile.getFileLink() + "'. ";
        }

        /* Check FK */
        if (oldFile.getUpdatedBy() != null) {
            if (!oldFile.getUpdatedBy().equals(updatedFile.getUpdatedBy())) {
                if (userService.existsById(updatedFile.getUpdatedBy())) {
                    errorMsg += "No User (UpdatedBy) found with Id: '" + updatedFile.getUpdatedBy()
                            + "'. Which violate constraint: FK_ExternalFile_User_UpdatedBy. ";
                }
            }
        } else {
            if (userService.existsById(updatedFile.getUpdatedBy())) {
                errorMsg += "No User (UpdatedBy) found with Id: '" + updatedFile.getUpdatedBy()
                        + "'. Which violate constraint: FK_ExternalFile_User_UpdatedBy. ";
            }
        }

        /* Check duplicate */
        if (externalFileRepository
                .existsByFileNameOrFileLinkAndFileIdIsNotAndStatusNotIn(
                        updatedFile.getFileName(),
                        updatedFile.getFileLink(),
                        updatedFile.getFileId(),
                        N_D_S_STATUS_LIST)) {
            errorMsg += "Already exists another ExternalFile with name: '" + updatedFile.getFileName()
                    + "', or with link: '" + updatedFile.getFileLink() + "'. ";
        }

        if (!errorMsg.trim().isEmpty())
            throw new IllegalArgumentException(errorMsg);

        updatedFile.setCreatedAt(oldFile.getCreatedAt());
        updatedFile.setCreatedBy(oldFile.getCreatedBy());

        return externalFileRepository.saveAndFlush(updatedFile);
    }
    @Override
    public ExternalFileReadDTO updateExternalFileByDTO(ExternalFileUpdateDTO updatedFileDTO) throws Exception {
        ExternalFile updatedFile = modelMapper.map(updatedFileDTO, ExternalFile.class);

        updatedFile = createExternalFile(updatedFile);

        if (updatedFile == null)
            return null;

        return fillDTO(updatedFile);
    }

    /* DELETE */
    @Override
    public boolean deleteExternalFile(long fileId) throws Exception {
        ExternalFile file = getById(fileId);

        if (file == null)
            return false;

        file.setStatus(Status.DELETED);
        externalFileRepository.saveAndFlush(file);

        return true;
    }

    @Override
    public boolean deleteAllByIdIn(Collection<Long> fileIdCollection) throws Exception {
        List<ExternalFile> fileList = getAllByIdIn(fileIdCollection);

        if (fileList == null)
            return false;


        fileList = fileList.stream()
                .peek(file -> file.setStatus(Status.DELETED))
                .collect(Collectors.toList());

        externalFileRepository.saveAllAndFlush(fileList);

        return true;
    }

    /* Utils */
    private ExternalFileReadDTO fillDTO(ExternalFile file) throws Exception {
        ExternalFileReadDTO fileDTO = modelMapper.map(file, ExternalFileReadDTO.class);

        /* Get associate fileType */
//        fileDTO.setFileType(
//                fileTypeService.getDTOById(file.getFileTypeId()));

        return fileDTO;
    }

    private List<ExternalFileReadDTO> fillAllDTO(Collection<ExternalFile> fileCollection, Integer totalPage) throws Exception {
//        Set<Long> fileTypeIdSet = new HashSet<>();
//
//        for (ExternalFile file : fileCollection) {
//            fileTypeIdSet.add(file.getFileTypeId());
//        }
//
//        /* Get associate fileType */
//        Map<Long, FileTypeReadDTO> fileTypeIdFileTypeDTOMap =
//                fileTypeService.mapFileTypeIdFileTypeDTOByIdIn(fileTypeIdSet);

        return fileCollection.stream()
                .map(file -> {
                    ExternalFileReadDTO fileDTO =
                            modelMapper.map(file, ExternalFileReadDTO.class);

//                    fileDTO.setFileType(fileTypeIdFileTypeDTOMap.get(file.getFileTypeId()));

                    fileDTO.setTotalPage(totalPage);

                    return fileDTO;})
                .collect(Collectors.toList());
    }
}
