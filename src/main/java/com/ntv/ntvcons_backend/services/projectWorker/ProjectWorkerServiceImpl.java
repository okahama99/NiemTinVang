package com.ntv.ntvcons_backend.services.projectWorker;

import com.ntv.ntvcons_backend.dtos.projectWorker.ProjectWorkerCreateDTO;
import com.ntv.ntvcons_backend.dtos.projectWorker.ProjectWorkerReadDTO;
import com.ntv.ntvcons_backend.dtos.projectWorker.ProjectWorkerUpdateDTO;
import com.ntv.ntvcons_backend.dtos.worker.WorkerReadDTO;
import com.ntv.ntvcons_backend.entities.ProjectWorker;
import com.ntv.ntvcons_backend.entities.ReportDetail;
import com.ntv.ntvcons_backend.repositories.ProjectWorkerRepository;
import com.ntv.ntvcons_backend.services.project.ProjectService;
import com.ntv.ntvcons_backend.services.user.UserService;
import com.ntv.ntvcons_backend.services.worker.WorkerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectWorkerServiceImpl implements ProjectWorkerService {
    @Autowired
    private ProjectWorkerRepository projectWorkerRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private DateTimeFormatter dateTimeFormatter;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private UserService userService;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private WorkerService workerService;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private ProjectService projectService;

    /* CREATE */
    @Override
    public ProjectWorker createProjectWorker(ProjectWorker newProjectWorker) throws Exception {
        String errorMsg = "";

        /* Check FK */
        if (!projectService.existsById(newProjectWorker.getProjectId())) {
            errorMsg += "No Project found with Id: '" + newProjectWorker.getProjectId()
                    + "'. Which violate constraint: FK_ProjectWorker_Project. ";
        }
        if (!workerService.existsById(newProjectWorker.getWorkerId())) {
            errorMsg += "No Worker (Worker) found with Id: '" + newProjectWorker.getWorkerId()
                    + "'. Which violate constraint: FK_ProjectWorker_Worker_WorkerId. ";
        }
        if (!userService.existsById(newProjectWorker.getCreatedBy())) {
            errorMsg += "No User (CreatedBy) found with Id: '" + newProjectWorker.getCreatedBy()
                    + "'. Which violate constraint: FK_ProjectWorker_User_CreatedBy. ";
        }

        /* Check duplicate */
        if (projectWorkerRepository
                .existsByProjectIdAndWorkerIdAndIsDeletedIsFalse(
                        newProjectWorker.getProjectId(),
                        newProjectWorker.getWorkerId())) {
            errorMsg += "Already exists another ProjectWorker relationship between with Project with Id: '"
                    + newProjectWorker.getProjectId()
                    + "' and Worker with Id: '"
                    + newProjectWorker.getWorkerId() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        return projectWorkerRepository.saveAndFlush(newProjectWorker);
    }
    @Override
    public ProjectWorkerReadDTO createProjectWorkerByDTO(ProjectWorkerCreateDTO newProjectWorkerDTO) throws Exception {
        /* TODO: use later or skip forever
        modelMapper.typeMap(ProjectWorkerCreateDTO.class, ProjectWorker.class)
                .addMappings(mapper -> {
                    mapper.skip(ProjectWorker::setAssignDate);});*/

        ProjectWorker newProjectWorker = modelMapper.map(newProjectWorkerDTO, ProjectWorker.class);

        newProjectWorker = createProjectWorker(newProjectWorker);

        return fillDTO(newProjectWorker);
    }

    @Override
    public List<ProjectWorker> createBulkProjectWorker(List<ProjectWorker> newProjectWorkerList) throws Exception {
        StringBuilder errorMsg = new StringBuilder();

        Map<Long, List<Long>> projectIdWorkerIdListMap = new HashMap<>();
        Set<Long> projectIdSet = new HashSet<>();
        Set<Long> workerIdSet = new HashSet<>();
        Set<Long> createdBySet = new HashSet<>();
        List<Long> tmpWorkerIdList;
        boolean isDuplicated = false;

        for (ProjectWorker newProjectWorker : newProjectWorkerList) {
            projectIdSet.add(newProjectWorker.getProjectId());
            workerIdSet.add(newProjectWorker.getWorkerId());
            createdBySet.add(newProjectWorker.getCreatedBy());

            /* Check duplicate 1 (within input) */
            tmpWorkerIdList = projectIdWorkerIdListMap.get(newProjectWorker.getProjectId());

            if (tmpWorkerIdList == null) {
                projectIdWorkerIdListMap.put(
                        newProjectWorker.getProjectId(),
                        new ArrayList<>(Collections.singletonList(newProjectWorker.getWorkerId())));
            } else {
                if (tmpWorkerIdList.contains(newProjectWorker.getWorkerId())) {
                    /* Already has workerId*/
                    isDuplicated = true;
                    errorMsg.append("Duplicate ProjectWorker relationship between Project with Id: '")
                            .append(newProjectWorker.getProjectId())
                            .append("' and Worker with Id: '")
                            .append(newProjectWorker.getWorkerId()).append("'. ");
                } else {
                   tmpWorkerIdList.add(newProjectWorker.getWorkerId());

                   projectIdWorkerIdListMap.put(newProjectWorker.getProjectId(), tmpWorkerIdList);
                }
            }
        }

        /* Check FK */
        if (!projectService.existsAllByIdIn(projectIdSet)) {
            errorMsg.append("1 or more Project not found with Id. Which violate constraint: FK_ProjectWorker_Project. ");
        }
        if (!workerService.existsAllByIdIn(workerIdSet)) {
            errorMsg.append("1 or more Worker not found with Id. Which violate constraint: FK_ProjectWorker_Worker_WorkerId. ");
        }
        if (!userService.existsAllByIdIn(createdBySet)) {
            errorMsg.append("1 or more User not found with Id. Which violate constraint: FK_ProjectWorker_User_CreatedBy. ");
        }

        /* If already has duplicated within input, no need to check with DB */
        if (!isDuplicated) {
            /* Check duplicate 2 (in DB) */
            for (ProjectWorker newProjectWorker : newProjectWorkerList) {
                /* TODO: bulk check instead of loop */
                if (projectWorkerRepository
                                .existsByProjectIdAndWorkerIdAndIsDeletedIsFalse(
                                        newProjectWorker.getProjectId(),
                                        newProjectWorker.getWorkerId())) {
                    errorMsg.append("Already exists another ProjectWorker relationship between with Project with Id: '")
                            .append(newProjectWorker.getProjectId())
                            .append("' and Worker with Id: '")
                            .append(newProjectWorker.getWorkerId()).append("'. ");
                }
            }
        }

        if (!errorMsg.toString().trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg.toString());
        }

        return projectWorkerRepository.saveAllAndFlush(newProjectWorkerList);
    }
    @Override
    public List<ProjectWorkerReadDTO> createBulkProjectWorkerByDTO(List<ProjectWorkerCreateDTO> newProjectWorkerDTOList) throws Exception {
        /* TODO: use later or skip forever
        modelMapper.typeMap(ProjectWorkerCreateDTO.class, ProjectWorker.class)
                .addMappings(mapper -> {
                    mapper.skip(ProjectWorker::setAssignDate);});*/

        List<ProjectWorker> projectWorkerList =
                newProjectWorkerDTOList.stream()
                        .map(projectWorkerDTO -> modelMapper.map(projectWorkerDTO, ProjectWorker.class))
                        .collect(Collectors.toList());

        projectWorkerList = createBulkProjectWorker(projectWorkerList);

        return fillAllDTO(projectWorkerList, null);
    }

    /* READ */
    @Override
    public Page<ProjectWorker> getPageAll(Pageable paging) throws Exception {
        Page<ProjectWorker> projectWorkerPage = projectWorkerRepository.findAllByIsDeletedIsFalse(paging);

        if (projectWorkerPage.isEmpty()) 
            return null;

        return projectWorkerPage;
    }
    @Override
    public List<ProjectWorkerReadDTO> getAllDTOInPaging(Pageable paging) throws Exception {
        Page<ProjectWorker> projectWorkerPage = getPageAll(paging);

        if (projectWorkerPage == null) 
            return null;

        List<ProjectWorker> projectWorkerList = projectWorkerPage.getContent();

        if (projectWorkerList.isEmpty()) 
            return null;

        return fillAllDTO(projectWorkerList, projectWorkerPage.getTotalPages());
    }

    @Override
    public ProjectWorker getById(long projectWorkerId) throws Exception {
        return projectWorkerRepository
                .findByProjectWorkerIdAndIsDeletedIsFalse(projectWorkerId)
                .orElse(null);
    }
    @Override
    public ProjectWorkerReadDTO getDTOById(long projectWorkerId) throws Exception {
        ProjectWorker projectWorker = getById(projectWorkerId);

        if (projectWorker == null) 
            return null;

        return fillDTO(projectWorker);
    }

    @Override
    public List<ProjectWorker> getAllByIdIn(Collection<Long> projectWorkerIdCollection) throws Exception {
        List<ProjectWorker> projectWorkerList =
                projectWorkerRepository.findAllByProjectWorkerIdInAndIsDeletedIsFalse(projectWorkerIdCollection);

        if (projectWorkerList.isEmpty()) 
            return null;

        return projectWorkerList;
    }
    @Override
    public List<ProjectWorkerReadDTO> getAllDTOByIdIn(Collection<Long> projectWorkerIdCollection) throws Exception {
        List<ProjectWorker> projectWorkerList = getAllByIdIn(projectWorkerIdCollection);

        if (projectWorkerList == null) 
            return null;

        return fillAllDTO(projectWorkerList, null);
    }

    @Override
    public List<ProjectWorker> getAllByWorkerId(long workerId) throws Exception {
        List<ProjectWorker> projectWorkerList =
                projectWorkerRepository.findAllByWorkerIdAndIsDeletedIsFalse(workerId);

        if (projectWorkerList.isEmpty()) 
            return null;

        return projectWorkerList;
    }
    @Override
    public List<ProjectWorkerReadDTO> getAllDTOByWorkerId(long workerId) throws Exception {
        List<ProjectWorker> projectWorkerList = getAllByWorkerId(workerId);

        if (projectWorkerList == null) 
            return null;

        return fillAllDTO(projectWorkerList, null);
    }
    @Override
    public Page<ProjectWorker> getPageAllByWorkerId(Pageable paging, long workerId) throws Exception {
        Page<ProjectWorker> projectWorkerPage =
                projectWorkerRepository.findAllByWorkerIdAndIsDeletedIsFalse(workerId, paging);

        if (projectWorkerPage.isEmpty()) 
            return null;

        return projectWorkerPage;
    }
    @Override
    public List<ProjectWorkerReadDTO> getAllDTOInPagingByWorkerId(Pageable paging, long workerId) throws Exception {
        Page<ProjectWorker> projectWorkerPage = getPageAllByWorkerId(paging, workerId);

        if (projectWorkerPage == null) 
            return null;

        List<ProjectWorker> projectWorkerList = projectWorkerPage.getContent();

        if (projectWorkerList.isEmpty()) 
            return null;

        return fillAllDTO(projectWorkerList, projectWorkerPage.getTotalPages());
    }

    @Override
    public List<ProjectWorker> getAllByWorkerIdIn(Collection<Long> workerIdCollection) throws Exception {
        List<ProjectWorker> projectWorkerList =
                projectWorkerRepository.findAllByWorkerIdInAndIsDeletedIsFalse(workerIdCollection);

        if (projectWorkerList.isEmpty()) 
            return null;

        return projectWorkerList;
    }
    @Override
    public List<ProjectWorkerReadDTO> getAllDTOByWorkerIdIn(Collection<Long> workerIdCollection) throws Exception {
        List<ProjectWorker> projectWorkerList = getAllByWorkerIdIn(workerIdCollection);

        if (projectWorkerList == null) 
            return null;

        return fillAllDTO(projectWorkerList, null);
    }
    @Override
    public Page<ProjectWorker> getPageAllByWorkerIdIn(Pageable paging, Collection<Long> workerIdCollection) throws Exception {
        Page<ProjectWorker> projectWorkerPage =
                projectWorkerRepository.findAllByWorkerIdInAndIsDeletedIsFalse(workerIdCollection, paging);

        if (projectWorkerPage.isEmpty()) 
            return null;

        return projectWorkerPage;
    }
    @Override
    public List<ProjectWorkerReadDTO> getAllDTOInPagingByWorkerIdIn(Pageable paging, Collection<Long> workerIdCollection) throws Exception {
        Page<ProjectWorker> projectWorkerPage = getPageAllByWorkerIdIn(paging, workerIdCollection);

        if (projectWorkerPage == null) 
            return null;

        List<ProjectWorker> projectWorkerList = projectWorkerPage.getContent();

        if (projectWorkerList.isEmpty()) 
            return null;

        return fillAllDTO(projectWorkerList, projectWorkerPage.getTotalPages());
    }

    @Override
    public List<ProjectWorker> getAllByProjectId(long projectId) throws Exception {
        List<ProjectWorker> projectWorkerList =
                projectWorkerRepository.findAllByProjectIdAndIsDeletedIsFalse(projectId);

        if (projectWorkerList.isEmpty()) 
            return null;

        return projectWorkerList;
    }
    @Override
    public List<ProjectWorkerReadDTO> getAllDTOByProjectId(long projectId) throws Exception {
        List<ProjectWorker> projectWorkerList = getAllByProjectId(projectId);

        if (projectWorkerList == null) 
            return null;

        return fillAllDTO(projectWorkerList, null);
    }
    @Override
    public Page<ProjectWorker> getPageAllByProjectId(Pageable paging, long projectId) throws Exception {
        Page<ProjectWorker> projectWorkerPage =
                projectWorkerRepository.findAllByProjectIdAndIsDeletedIsFalse(projectId, paging);

        if (projectWorkerPage.isEmpty()) 
            return null;

        return projectWorkerPage;
    }
    @Override
    public List<ProjectWorkerReadDTO> getAllDTOInPagingByProjectId(Pageable paging, long projectId) throws Exception {
        Page<ProjectWorker> projectWorkerPage = getPageAllByProjectId(paging, projectId);

        if (projectWorkerPage == null) 
            return null;

        List<ProjectWorker> projectWorkerList = projectWorkerPage.getContent();

        if (projectWorkerList.isEmpty()) 
            return null;

        return fillAllDTO(projectWorkerList, projectWorkerPage.getTotalPages());
    }

    @Override
    public List<ProjectWorker> getAllByProjectIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<ProjectWorker> projectWorkerList =
                projectWorkerRepository.findAllByProjectIdInAndIsDeletedIsFalse(projectIdCollection);

        if (projectWorkerList.isEmpty()) 
            return null;

        return projectWorkerList;
    }
    @Override
    public List<ProjectWorkerReadDTO> getAllDTOByProjectIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<ProjectWorker> projectWorkerList = getAllByProjectIdIn(projectIdCollection);

        if (projectWorkerList == null) 
            return null;

        return fillAllDTO(projectWorkerList, null);
    }
    @Override
    public Map<Long, List<ProjectWorkerReadDTO>> mapProjectIdProjectWorkerDTOListByProjectIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<ProjectWorkerReadDTO> projectWorkerDTOList = getAllDTOByProjectIdIn(projectIdCollection);

        if (projectWorkerDTOList == null) 
            return new HashMap<>();

        Map<Long, List<ProjectWorkerReadDTO>> projectIdProjectWorkerDTOListMap = new HashMap<>();

        long tmpProjectId;
        List<ProjectWorkerReadDTO> tmpProjectWorkerDTOList;

        for (ProjectWorkerReadDTO projectWorkerDTO : projectWorkerDTOList) {

            tmpProjectId = projectWorkerDTO.getProjectId();
            tmpProjectWorkerDTOList = projectIdProjectWorkerDTOListMap.get(tmpProjectId);

            if (tmpProjectWorkerDTOList == null) {
                projectIdProjectWorkerDTOListMap
                        .put(tmpProjectId, new ArrayList<>(Collections.singletonList(projectWorkerDTO)));
            } else {
                tmpProjectWorkerDTOList.add(projectWorkerDTO);

                projectIdProjectWorkerDTOListMap.put(tmpProjectId, tmpProjectWorkerDTOList);
            }
        }

        return projectIdProjectWorkerDTOListMap;
    }
    @Override
    public Page<ProjectWorker> getPageAllByProjectIdIn(Pageable paging, Collection<Long> projectIdCollection) throws Exception {
        Page<ProjectWorker> projectWorkerPage =
                projectWorkerRepository.findAllByProjectIdInAndIsDeletedIsFalse(projectIdCollection, paging);

        if (projectWorkerPage.isEmpty()) 
            return null;

        return projectWorkerPage;
    }
    @Override
    public List<ProjectWorkerReadDTO> getAllDTOInPagingByProjectIdIn(Pageable paging, Collection<Long> projectIdCollection) throws Exception {
        Page<ProjectWorker> projectWorkerPage = getPageAllByProjectIdIn(paging, projectIdCollection);

        if (projectWorkerPage == null) 
            return null;

        List<ProjectWorker> projectWorkerList = projectWorkerPage.getContent();

        if (projectWorkerList.isEmpty()) 
            return null;

        return fillAllDTO(projectWorkerList, projectWorkerPage.getTotalPages());
    }

    /* UPDATE */
    @Override
    public ProjectWorker updateProjectWorker(ProjectWorker updatedProjectWorker) throws Exception {
        ProjectWorker oldProjectWorker = getById(updatedProjectWorker.getProjectWorkerId());

        if (oldProjectWorker == null) 
            return null;

        String errorMsg = "";

        /* Check input */
        if (updatedProjectWorker.getRemoveDate() != null) {
            if (updatedProjectWorker.getRemoveDate().isBefore(oldProjectWorker.getAssignDate())) {
                errorMsg += "Invalid Input: removeDate is before assignDate. ";
            }
        }

        /* Check FK (if changed) */
        if (!oldProjectWorker.getProjectId().equals(updatedProjectWorker.getProjectId())) {
            if (!projectService.existsById(updatedProjectWorker.getProjectId())) {
                errorMsg += "No Project found with Id: '" + updatedProjectWorker.getProjectId()
                        + "'. Which violate constraint: FK_ProjectWorker_Project. ";
            }
        }
        if (!oldProjectWorker.getWorkerId().equals(updatedProjectWorker.getWorkerId())) {
            if (!workerService.existsById(updatedProjectWorker.getWorkerId())) {
                errorMsg += "No Worker (Worker) found with Id: '" + updatedProjectWorker.getWorkerId()
                        + "'. Which violate constraint: FK_ProjectWorker_Worker_WorkerId. ";
            }
        }
        if (oldProjectWorker.getUpdatedBy() != null) {
            if (!oldProjectWorker.getUpdatedBy().equals(updatedProjectWorker.getUpdatedBy())) {
                if (!workerService.existsById(updatedProjectWorker.getUpdatedBy())) {
                    errorMsg += "No User (UpdatedBy) found with Id: '" + updatedProjectWorker.getUpdatedBy()
                            + "'. Which violate constraint: FK_ProjectWorker_User_UpdatedBy. ";
                }
            }
        } else {
            if (!workerService.existsById(updatedProjectWorker.getUpdatedBy())) {
                errorMsg += "No User (UpdatedBy) found with Id: '" + updatedProjectWorker.getUpdatedBy()
                        + "'. Which violate constraint: FK_ProjectWorker_User_UpdatedBy. ";
            }
        }

        /* Check duplicate */
        if (projectWorkerRepository
                .existsByProjectIdAndWorkerIdAndProjectWorkerIdIsNotAndIsDeletedIsFalse(
                        updatedProjectWorker.getProjectId(),
                        updatedProjectWorker.getWorkerId(),
                        updatedProjectWorker.getProjectWorkerId())) {
            errorMsg += "Already exists another ProjectWorker relationship between with Project with Id: '"
                    + updatedProjectWorker.getProjectId()
                    + "' and Worker with Id: '"
                    + updatedProjectWorker.getWorkerId() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        return projectWorkerRepository.saveAndFlush(updatedProjectWorker);
    }
    @Override
    public ProjectWorkerReadDTO updateProjectWorkerByDTO(ProjectWorkerUpdateDTO updatedProjectWorkerDTO) throws Exception {
        modelMapper.typeMap(ProjectWorkerUpdateDTO.class, ProjectWorker.class)
                .addMappings(mapper -> {
                    mapper.skip(ProjectWorker::setAssignDate);
                    mapper.skip(ProjectWorker::setRemoveDate);});

        ProjectWorker updatedProjectWorker =
                modelMapper.map(updatedProjectWorkerDTO, ProjectWorker.class);

        boolean updateAssignDate = false;
        if (updatedProjectWorkerDTO.getRemoveDate() != null) {
            updateAssignDate = true;

            updatedProjectWorker.setAssignDate(
                    LocalDateTime.parse(updatedProjectWorkerDTO.getAssignDate(), dateTimeFormatter));
        }

        if (updatedProjectWorkerDTO.getRemoveDate() != null) {
            updatedProjectWorker.setRemoveDate(
                    LocalDateTime.parse(updatedProjectWorkerDTO.getRemoveDate(), dateTimeFormatter));

            if (updateAssignDate) {
                if (updatedProjectWorker.getRemoveDate().isBefore(updatedProjectWorker.getAssignDate())) {
                    throw new IllegalArgumentException("removeDate is before assignDate");
                }
            }
        }

        updatedProjectWorker = updateProjectWorker(updatedProjectWorker);

        if (updatedProjectWorker == null) 
            return null;

        return fillDTO(updatedProjectWorker);
    }

    @Override
    public List<ProjectWorker> updateBulkProjectWorker(List<ProjectWorker> updatedProjectWorkerList) throws Exception {
        Set<Long> projectWorkerIdSet = new HashSet<>();
        Map<Long, List<Long>> projectIdWorkerIdMap = new HashMap<>();

        Set<Long> updatedProjectIdSet = new HashSet<>();
        Set<Long> updatedWorkerIdSet = new HashSet<>();
        Set<Long> updatedUpdatedBySet = new HashSet<>();

        Map<Long, ProjectWorker> projectWorkerIdUpdatedProjectWorkerMap = new HashMap<>();

        List<Long> tmpWorkerIdList;
        boolean isDuplicated = false;

        StringBuilder errorMsg = new StringBuilder();

        for (ProjectWorker updatedProjectWorker : updatedProjectWorkerList) {
            projectWorkerIdSet.add(updatedProjectWorker.getProjectWorkerId());
            updatedProjectIdSet.add(updatedProjectWorker.getProjectId());
            updatedWorkerIdSet.add(updatedProjectWorker.getWorkerId());
            updatedUpdatedBySet.add(updatedProjectWorker.getWorkerId());

            projectWorkerIdUpdatedProjectWorkerMap
                    .put(updatedProjectWorker.getProjectWorkerId(), updatedProjectWorker);

            /* Check duplicate 1 (within input) */
            tmpWorkerIdList = projectIdWorkerIdMap.get(updatedProjectWorker.getProjectId());
            if (tmpWorkerIdList == null) {
                projectIdWorkerIdMap.put(
                        updatedProjectWorker.getProjectId(),
                        new ArrayList<>(Collections.singletonList(updatedProjectWorker.getWorkerId())));
            } else {
                if (tmpWorkerIdList.contains(updatedProjectWorker.getWorkerId())) {
                    isDuplicated = true;

                    errorMsg.append("Duplicate ProjectWorker relationship between with Project with Id: '")
                            .append(updatedProjectWorker.getProjectId())
                            .append("' and Worker with Id: '")
                            .append(updatedProjectWorker.getWorkerId()).append("'. ");
                } else {
                    tmpWorkerIdList.add(updatedProjectWorker.getWorkerId());
                    projectIdWorkerIdMap.put(updatedProjectWorker.getProjectId(), tmpWorkerIdList);
                }
            }
        }

        List<ProjectWorker> oldProjectWorkerList = getAllByIdIn(projectWorkerIdSet);

        if (oldProjectWorkerList == null) 
            return null;

        Set<Long> oldProjectIdSet = new HashSet<>();
        Set<Long> oldWorkerIdSet = new HashSet<>();
        Set<Long> oldUpdatedBySet = new HashSet<>();

        Map<Long, Long> projectWorkerIdCreatedByMap = new HashMap<>();
        Map<Long, LocalDateTime> projectWorkerIdCreatedAtMap = new HashMap<>();

        for (ProjectWorker oldProjectWorker : oldProjectWorkerList) {
            oldProjectIdSet.add(oldProjectWorker.getProjectId());
            oldWorkerIdSet.add(oldProjectWorker.getWorkerId());
            if (oldProjectWorker.getUpdatedBy() != null) {
                oldUpdatedBySet.add(oldProjectWorker.getWorkerId());
            }

            projectWorkerIdCreatedByMap.put(oldProjectWorker.getProjectWorkerId(), oldProjectWorker.getCreatedBy());
            projectWorkerIdCreatedAtMap.put(oldProjectWorker.getProjectWorkerId(), oldProjectWorker.getCreatedAt());

            ProjectWorker updatedProjectWorker =
                    projectWorkerIdUpdatedProjectWorkerMap.get(oldProjectWorker.getProjectWorkerId());

            if (updatedProjectWorker.getAssignDate() == null) {
                if (updatedProjectWorker.getRemoveDate() != null){
                    if (updatedProjectWorker.getRemoveDate().isBefore(oldProjectWorker.getAssignDate())) {
                        errorMsg.append("Invalid Input: removeDate is before assignDate. ")
                                .append("At ProjectWorker with id: '")
                                .append(oldProjectWorker.getProjectWorkerId())
                                .append("'. ");
                    }
                }

                updatedProjectWorker.setAssignDate(oldProjectWorker.getAssignDate());

                projectWorkerIdUpdatedProjectWorkerMap
                        .put(oldProjectWorker.getProjectWorkerId(), updatedProjectWorker);
            }
        }

        /* Remove all unchanged projectId & workerId */
        updatedProjectIdSet.removeAll(oldProjectIdSet);
        updatedWorkerIdSet.removeAll(oldWorkerIdSet);
        updatedUpdatedBySet.removeAll(oldUpdatedBySet);

        /* Check FK (if changed) */
        /* If there are updated projectId, need to recheck FK */
        if (!updatedProjectIdSet.isEmpty()) {
            if (!projectService.existsAllByIdIn(updatedProjectIdSet)) {
                errorMsg.append("1 or more Project not found with Id. ")
                        .append("Which violate constraint: FK_ProjectWorker_Project. ");
            }
        }
        /* If there are updated workerId, need to recheck FK */
        if (!updatedWorkerIdSet.isEmpty()) {
            if (!workerService.existsAllByIdIn(updatedWorkerIdSet)) {
                errorMsg.append("1 or more Worker not found with Id. ")
                        .append("Which violate constraint: FK_ProjectWorker_Worker_WorkerId. ");
            }
        }
        if (!updatedUpdatedBySet.isEmpty()) {
            if (!workerService.existsAllByIdIn(updatedUpdatedBySet)) {
                errorMsg.append("1 or more Worker not found with Id. ")
                        .append("Which violate constraint: FK_ProjectWorker_Worker_UpdatedBy. ");
            }
        }

        /* Already has duplicated within input, no need to check with DB */
        if (!isDuplicated) {
            /* Check duplicate 2 (in DB) */
            for (ProjectWorker updatedProjectWorker : updatedProjectWorkerList) {
                /* TODO: bulk check instead of loop */
                if (projectWorkerRepository
                        .existsByProjectIdAndWorkerIdAndProjectWorkerIdIsNotAndIsDeletedIsFalse(
                                updatedProjectWorker.getProjectId(),
                                updatedProjectWorker.getWorkerId(),
                                updatedProjectWorker.getProjectWorkerId())) {
                    errorMsg.append("Already exists another ProjectWorker relationship between Project with Id: '")
                            .append(updatedProjectWorker.getProjectId())
                            .append("' and Worker with Id: '")
                            .append(updatedProjectWorker.getWorkerId()).append("'. ");
                }
            }
        }

        if (!errorMsg.toString().trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg.toString());
        }

        updatedProjectWorkerList =
                updatedProjectWorkerList.stream()
                        .peek(projectWorker -> {
                            projectWorker.setCreatedAt(
                                    projectWorkerIdCreatedAtMap.get(projectWorker.getProjectWorkerId()));

                            projectWorker.setCreatedBy(
                                    projectWorkerIdCreatedByMap.get(projectWorker.getProjectWorkerId()));})
                        .collect(Collectors.toList());

        return projectWorkerRepository.saveAllAndFlush(updatedProjectWorkerList);
    }
    @Override
    public List<ProjectWorkerReadDTO> updateBulkProjectWorkerByDTO(List<ProjectWorkerUpdateDTO> updatedProjectWorkerDTOList) throws Exception {
        modelMapper.typeMap(ProjectWorkerCreateDTO.class, ProjectWorker.class)
                .addMappings(mapper -> {
                    mapper.skip(ProjectWorker::setAssignDate);
                    mapper.skip(ProjectWorker::setRemoveDate);});

        StringBuilder errorMsg = new StringBuilder();
        
        List<ProjectWorker> projectWorkerList =
                updatedProjectWorkerDTOList.stream()
                        .map(projectWorkerDTO -> {
                            ProjectWorker projectWorker =
                                    modelMapper.map(projectWorkerDTO, ProjectWorker.class);

                            boolean updateAssignDate = false;
                            if (projectWorkerDTO.getRemoveDate() != null) {
                                updateAssignDate = true;

                                projectWorker.setAssignDate(
                                        LocalDateTime.parse(projectWorkerDTO.getAssignDate(), dateTimeFormatter));
                            }

                            if (projectWorkerDTO.getRemoveDate() != null) {
                                projectWorker.setRemoveDate(
                                        LocalDateTime.parse(projectWorkerDTO.getRemoveDate(), dateTimeFormatter));

                                if (updateAssignDate) {
                                    if (projectWorker.getRemoveDate().isBefore(projectWorker.getAssignDate())) {
                                        errorMsg.append("Invalid input: removeDate is before assignDate. ")
                                                .append("At ProjectWorker with id: '")
                                                .append(projectWorker.getProjectWorkerId())
                                                .append("'. ");
                                    }
                                }
                            }

                            return projectWorker;})
                        .collect(Collectors.toList());

        if (!errorMsg.toString().trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg.toString());
        }

        projectWorkerList = updateBulkProjectWorker(projectWorkerList);

        if (projectWorkerList == null)
            return null;

        return fillAllDTO(projectWorkerList, null);
    }

    /* DELETE */
    @Override
    public boolean deleteProjectWorker(long projectWorkerId) throws Exception {
        ProjectWorker projectWorker = getById(projectWorkerId);

        if (projectWorker == null) {
            return false;
        }

        projectWorker.setIsDeleted(true);
        projectWorkerRepository.saveAndFlush(projectWorker);

        return true;
    }

    @Override
    public boolean deleteAllByWorkerId(long workerId) throws Exception {
        List<ProjectWorker> projectWorkerList = getAllByWorkerId(workerId);

        if (projectWorkerList == null) {
            return false;
        }

        projectWorkerList =
                projectWorkerList.stream()
                        .peek(projectWorker -> projectWorker.setIsDeleted(true))
                        .collect(Collectors.toList());

        projectWorkerRepository.saveAllAndFlush(projectWorkerList);

        return true;
    }

    @Override
    public boolean deleteAllByProjectId(long projectId) throws Exception {
        List<ProjectWorker> projectWorkerList = getAllByProjectId(projectId);

        if (projectWorkerList == null) {
            return false;
        }

        projectWorkerList =
                projectWorkerList.stream()
                        .peek(projectWorker -> projectWorker.setIsDeleted(true))
                        .collect(Collectors.toList());

        projectWorkerRepository.saveAllAndFlush(projectWorkerList);

        return true;
    }

    /* Utils */
    private ProjectWorkerReadDTO fillDTO(ProjectWorker projectWorker) throws Exception {
        ProjectWorkerReadDTO projectWorkerDTO =
                modelMapper.map(projectWorker, ProjectWorkerReadDTO.class);

        /* Get associated Worker (Worker) */
        projectWorkerDTO.setWorker(workerService.getDTOById(projectWorker.getWorkerId()));

        return projectWorkerDTO;
    }

    private List<ProjectWorkerReadDTO> fillAllDTO(List<ProjectWorker> projectWorkerList, Integer totalPage) throws Exception {
        Set<Long> workerIdSet =
                projectWorkerList.stream()
                        .map(ProjectWorker::getWorkerId)
                        .collect(Collectors.toSet());

        /* Get associated Worker (Worker) */
        Map<Long, WorkerReadDTO> workerIdWorkerDTOMap = workerService.mapWorkerIdWorkerDTOByIdIn(workerIdSet);

        return projectWorkerList.stream()
                .map(projectWorker -> {
                    ProjectWorkerReadDTO projectWorkerDTO =
                            modelMapper.map(projectWorker, ProjectWorkerReadDTO.class);

                    projectWorkerDTO.setWorker(workerIdWorkerDTOMap.get(projectWorker.getWorkerId()));

                    projectWorkerDTO.setTotalPage(totalPage);

                    return projectWorkerDTO;})
                .collect(Collectors.toList());
    }
}
