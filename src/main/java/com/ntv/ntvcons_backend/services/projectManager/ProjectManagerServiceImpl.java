package com.ntv.ntvcons_backend.services.projectManager;

import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerCreateDTO;
import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerReadDTO;
import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerUpdateDTO;
import com.ntv.ntvcons_backend.dtos.user.UserReadDTO;
import com.ntv.ntvcons_backend.entities.ProjectManager;
import com.ntv.ntvcons_backend.repositories.ProjectManagerRepository;
import com.ntv.ntvcons_backend.services.project.ProjectService;
import com.ntv.ntvcons_backend.services.user.UserService;
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
public class ProjectManagerServiceImpl implements ProjectManagerService {
    @Autowired
    private ProjectManagerRepository projectManagerRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private DateTimeFormatter dateTimeFormatter;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private UserService userService;
    @Lazy /* To avoid circular injection Exception */
    @Autowired
    private ProjectService projectService;

    private final List<Status> N_D_S_STATUS_LIST = Status.getAllNonDefaultSearchStatus();

    /* CREATE */
    @Override
    public ProjectManager createProjectManager(ProjectManager newProjectManager) throws Exception {
        String errorMsg = "";

        /* Check FK */
        if (!projectService.existsById(newProjectManager.getProjectId())) {
            errorMsg += "No Project found with Id: '" + newProjectManager.getProjectId()
                    + "'. Which violate constraint: FK_ProjectManager_Project. ";
        }
        if (!userService.existsById(newProjectManager.getManagerId())) {
            errorMsg += "No User (Manager) found with Id: '" + newProjectManager.getManagerId()
                    + "'. Which violate constraint: FK_ProjectManager_User_ManagerId. ";
        }
        if (!userService.existsById(newProjectManager.getCreatedBy())) {
            errorMsg += "No User (CreatedBy) found with Id: '" + newProjectManager.getCreatedBy()
                    + "'. Which violate constraint: FK_ProjectManager_User_CreatedBy. ";
        }

        /* Check duplicate */
        if (projectManagerRepository
                .existsByProjectIdAndManagerIdAndStatusNotIn(
                        newProjectManager.getProjectId(),
                        newProjectManager.getManagerId(),
                        N_D_S_STATUS_LIST)) {
            errorMsg += "Already exists another ProjectManager relationship between with Project with Id: '"
                    + newProjectManager.getProjectId()
                    + "' and User with Id: '"
                    + newProjectManager.getManagerId() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        return projectManagerRepository.saveAndFlush(newProjectManager);
    }
    @Override
    public ProjectManagerReadDTO createProjectManagerByDTO(ProjectManagerCreateDTO newProjectManagerDTO) throws Exception {
        /* TODO: use later or skip forever
        modelMapper.typeMap(ProjectManagerCreateDTO.class, ProjectManager.class)
                .addMappings(mapper -> {
                    mapper.skip(ProjectManager::setAssignDate);});*/

        ProjectManager newProjectManager = modelMapper.map(newProjectManagerDTO, ProjectManager.class);

        newProjectManager = createProjectManager(newProjectManager);

        return fillDTO(newProjectManager);
    }

    @Override
    public List<ProjectManager> createBulkProjectManager(List<ProjectManager> newProjectManagerList) throws Exception {
        StringBuilder errorMsg = new StringBuilder();

        Map<Long, List<Long>> projectIdManagerIdListMap = new HashMap<>();
        Set<Long> projectIdSet = new HashSet<>();
        Set<Long> managerIdSet = new HashSet<>();
        Set<Long> createdBySet = new HashSet<>();
        List<Long> tmpManagerIdList;
        boolean isDuplicated = false;

        for (ProjectManager newProjectManager : newProjectManagerList) {
            projectIdSet.add(newProjectManager.getProjectId());
            managerIdSet.add(newProjectManager.getManagerId());
            createdBySet.add(newProjectManager.getCreatedBy());

            /* Check duplicate 1 (within input) */
            tmpManagerIdList = projectIdManagerIdListMap.get(newProjectManager.getProjectId());

            if (tmpManagerIdList == null) {
                projectIdManagerIdListMap.put(
                        newProjectManager.getProjectId(),
                        new ArrayList<>(Collections.singletonList(newProjectManager.getManagerId())));
            } else {
                if (tmpManagerIdList.contains(newProjectManager.getManagerId())) {
                    /* Already has managerId*/
                    isDuplicated = true;
                    errorMsg.append("Duplicate ProjectManager relationship between Project with Id: '")
                            .append(newProjectManager.getProjectId())
                            .append("' and User with Id: '")
                            .append(newProjectManager.getManagerId()).append("'. ");
                } else {
                       tmpManagerIdList.add(newProjectManager.getManagerId());
                       projectIdManagerIdListMap.put(newProjectManager.getProjectId(), tmpManagerIdList);
                }
            }
        }

        /* Check FK */
        if (!projectService.existsAllByIdIn(projectIdSet)) {
            errorMsg.append("1 or more Project not found with Id. Which violate constraint: FK_ProjectManager_Project. ");
        }
        if (!userService.existsAllByIdIn(managerIdSet)) {
            errorMsg.append("1 or more User not found with Id. Which violate constraint: FK_ProjectManager_User_ManagerId. ");
        }
        if (!userService.existsAllByIdIn(createdBySet)) {
            errorMsg.append("1 or more User not found with Id. Which violate constraint: FK_ProjectManager_User_CreatedBy. ");
        }

        /* If already has duplicated within input, no need to check with DB */
        if (!isDuplicated) {
            /* Check duplicate 2 (in DB) */
            for (ProjectManager newProjectManager : newProjectManagerList) {
                /* TODO: bulk check instead of loop */
                if (projectManagerRepository
                                .existsByProjectIdAndManagerIdAndStatusNotIn(
                                        newProjectManager.getProjectId(),
                                        newProjectManager.getManagerId(),
                                        N_D_S_STATUS_LIST)) {
                    errorMsg.append("Already exists another ProjectManager relationship between with Project with Id: '")
                            .append(newProjectManager.getProjectId())
                            .append("' and User with Id: '")
                            .append(newProjectManager.getManagerId()).append("'. ");
                }
            }
        }

        if (!errorMsg.toString().trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg.toString());
        }

        return projectManagerRepository.saveAllAndFlush(newProjectManagerList);
    }
    @Override
    public List<ProjectManagerReadDTO> createBulkProjectManagerByDTO(List<ProjectManagerCreateDTO> newProjectManagerDTOList) throws Exception {
        /* TODO: use later or skip forever
        modelMapper.typeMap(ProjectManagerCreateDTO.class, ProjectManager.class)
                .addMappings(mapper -> {
                    mapper.skip(ProjectManager::setAssignDate);});*/

        List<ProjectManager> projectManagerList =
                newProjectManagerDTOList.stream()
                        .map(projectManagerDTO -> modelMapper.map(projectManagerDTO, ProjectManager.class))
                        .collect(Collectors.toList());

        projectManagerList = createBulkProjectManager(projectManagerList);

        return fillAllDTO(projectManagerList, null);
    }

    /* READ */
    @Override
    public Page<ProjectManager> getPageAll(Pageable paging) throws Exception {
        Page<ProjectManager> projectManagerPage =
                projectManagerRepository.findAllByStatusNotIn(N_D_S_STATUS_LIST, paging);

        if (projectManagerPage.isEmpty()) 
            return null;

        return projectManagerPage;
    }
    @Override
    public List<ProjectManagerReadDTO> getAllDTOInPaging(Pageable paging) throws Exception {
        Page<ProjectManager> projectManagerPage = getPageAll(paging);

        if (projectManagerPage == null) 
            return null;

        List<ProjectManager> projectManagerList = projectManagerPage.getContent();

        if (projectManagerList.isEmpty()) 
            return null;

        return fillAllDTO(projectManagerList, projectManagerPage.getTotalPages());
    }

    @Override
    public ProjectManager getById(long projectManagerId) throws Exception {
        return projectManagerRepository
                .findByProjectManagerIdAndStatusNotIn(projectManagerId, N_D_S_STATUS_LIST)
                .orElse(null);
    }
    @Override
    public ProjectManagerReadDTO getDTOById(long projectManagerId) throws Exception {
        ProjectManager projectManager = getById(projectManagerId);

        if (projectManager == null) 
            return null;

        return fillDTO(projectManager);
    }

    @Override
    public List<ProjectManager> getAllByIdIn(Collection<Long> projectManagerIdCollection) throws Exception {
        List<ProjectManager> projectManagerList =
                projectManagerRepository
                        .findAllByProjectManagerIdInAndStatusNotIn(projectManagerIdCollection, N_D_S_STATUS_LIST);

        if (projectManagerList.isEmpty()) 
            return null;

        return projectManagerList;
    }
    @Override
    public List<ProjectManagerReadDTO> getAllDTOByIdIn(Collection<Long> projectManagerIdCollection) throws Exception {
        List<ProjectManager> projectManagerList = getAllByIdIn(projectManagerIdCollection);

        if (projectManagerList == null) 
            return null;

        return fillAllDTO(projectManagerList, null);
    }

    @Override
    public List<ProjectManager> getAllByManagerId(long managerId) throws Exception {
        List<ProjectManager> projectManagerList =
                projectManagerRepository.findAllByManagerIdAndStatusNotIn(managerId, N_D_S_STATUS_LIST);

        if (projectManagerList.isEmpty()) 
            return null;

        return projectManagerList;
    }
    @Override
    public List<ProjectManagerReadDTO> getAllDTOByManagerId(long managerId) throws Exception {
        List<ProjectManager> projectManagerList = getAllByManagerId(managerId);

        if (projectManagerList == null) 
            return null;

        return fillAllDTO(projectManagerList, null);
    }
    @Override
    public Page<ProjectManager> getPageAllByManagerId(Pageable paging, long managerId) throws Exception {
        Page<ProjectManager> projectManagerPage =
                projectManagerRepository.findAllByManagerIdAndStatusNotIn(managerId, N_D_S_STATUS_LIST, paging);

        if (projectManagerPage.isEmpty()) 
            return null;

        return projectManagerPage;
    }
    @Override
    public List<ProjectManagerReadDTO> getAllDTOInPagingByManagerId(Pageable paging, long managerId) throws Exception {
        Page<ProjectManager> projectManagerPage = getPageAllByManagerId(paging, managerId);

        if (projectManagerPage == null) 
            return null;

        List<ProjectManager> projectManagerList = projectManagerPage.getContent();

        if (projectManagerList.isEmpty()) 
            return null;

        return fillAllDTO(projectManagerList, projectManagerPage.getTotalPages());
    }

    @Override
    public List<ProjectManager> getAllByManagerIdIn(Collection<Long> managerIdCollection) throws Exception {
        List<ProjectManager> projectManagerList =
                projectManagerRepository.findAllByManagerIdInAndStatusNotIn(managerIdCollection, N_D_S_STATUS_LIST);

        if (projectManagerList.isEmpty()) 
            return null;

        return projectManagerList;
    }
    @Override
    public List<ProjectManagerReadDTO> getAllDTOByManagerIdIn(Collection<Long> managerIdCollection) throws Exception {
        List<ProjectManager> projectManagerList = getAllByManagerIdIn(managerIdCollection);

        if (projectManagerList == null) 
            return null;

        return fillAllDTO(projectManagerList, null);
    }
    @Override
    public Page<ProjectManager> getPageAllByManagerIdIn(Pageable paging, Collection<Long> managerIdCollection) throws Exception {
        Page<ProjectManager> projectManagerPage =
                projectManagerRepository
                        .findAllByManagerIdInAndStatusNotIn(managerIdCollection, N_D_S_STATUS_LIST, paging);

        if (projectManagerPage.isEmpty()) 
            return null;

        return projectManagerPage;
    }
    @Override
    public List<ProjectManagerReadDTO> getAllDTOInPagingByManagerIdIn(Pageable paging, Collection<Long> managerIdCollection) throws Exception {
        Page<ProjectManager> projectManagerPage = getPageAllByManagerIdIn(paging, managerIdCollection);

        if (projectManagerPage == null) 
            return null;

        List<ProjectManager> projectManagerList = projectManagerPage.getContent();

        if (projectManagerList.isEmpty()) 
            return null;

        return fillAllDTO(projectManagerList, projectManagerPage.getTotalPages());
    }

    @Override
    public List<ProjectManager> getAllByProjectId(long projectId) throws Exception {
        List<ProjectManager> projectManagerList =
                projectManagerRepository.findAllByProjectIdAndStatusNotIn(projectId, N_D_S_STATUS_LIST);

        if (projectManagerList.isEmpty()) 
            return null;

        return projectManagerList;
    }
    @Override
    public List<ProjectManagerReadDTO> getAllDTOByProjectId(long projectId) throws Exception {
        List<ProjectManager> projectManagerList = getAllByProjectId(projectId);

        if (projectManagerList == null) 
            return null;

        return fillAllDTO(projectManagerList, null);
    }
    @Override
    public Page<ProjectManager> getPageAllByProjectId(Pageable paging, long projectId) throws Exception {
        Page<ProjectManager> projectManagerPage =
                projectManagerRepository.findAllByProjectIdAndStatusNotIn(projectId, N_D_S_STATUS_LIST, paging);

        if (projectManagerPage.isEmpty()) 
            return null;

        return projectManagerPage;
    }
    @Override
    public List<ProjectManagerReadDTO> getAllDTOInPagingByProjectId(Pageable paging, long projectId) throws Exception {
        Page<ProjectManager> projectManagerPage = getPageAllByProjectId(paging, projectId);

        if (projectManagerPage == null) 
            return null;

        List<ProjectManager> projectManagerList = projectManagerPage.getContent();

        if (projectManagerList.isEmpty()) 
            return null;

        return fillAllDTO(projectManagerList, projectManagerPage.getTotalPages());
    }

    @Override
    public List<ProjectManager> getAllByProjectIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<ProjectManager> projectManagerList =
                projectManagerRepository.findAllByProjectIdInAndStatusNotIn(projectIdCollection, N_D_S_STATUS_LIST);

        if (projectManagerList.isEmpty()) 
            return null;

        return projectManagerList;
    }
    @Override
    public List<ProjectManagerReadDTO> getAllDTOByProjectIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<ProjectManager> projectManagerList = getAllByProjectIdIn(projectIdCollection);

        if (projectManagerList == null) 
            return null;

        return fillAllDTO(projectManagerList, null);
    }
    @Override
    public Map<Long, List<ProjectManagerReadDTO>> mapProjectIdProjectManagerDTOListByProjectIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<ProjectManagerReadDTO> projectManagerDTOList = getAllDTOByProjectIdIn(projectIdCollection);

        if (projectManagerDTOList == null) 
            return new HashMap<>();

        Map<Long, List<ProjectManagerReadDTO>> projectIdProjectManagerDTOListMap = new HashMap<>();

        long tmpProjectId;
        List<ProjectManagerReadDTO> tmpProjectManagerDTOList;

        for (ProjectManagerReadDTO projectManagerDTO : projectManagerDTOList) {

            tmpProjectId = projectManagerDTO.getProjectId();
            tmpProjectManagerDTOList = projectIdProjectManagerDTOListMap.get(tmpProjectId);

            if (tmpProjectManagerDTOList == null) {
                projectIdProjectManagerDTOListMap
                        .put(tmpProjectId, new ArrayList<>(Collections.singletonList(projectManagerDTO)));
            } else {
                tmpProjectManagerDTOList.add(projectManagerDTO);

                projectIdProjectManagerDTOListMap.put(tmpProjectId, tmpProjectManagerDTOList);
            }
        }

        return projectIdProjectManagerDTOListMap;
    }
    @Override
    public Page<ProjectManager> getPageAllByProjectIdIn(Pageable paging, Collection<Long> projectIdCollection) throws Exception {
        Page<ProjectManager> projectManagerPage =
                projectManagerRepository.findAllByProjectIdInAndStatusNotIn(projectIdCollection, N_D_S_STATUS_LIST, paging);

        if (projectManagerPage.isEmpty()) 
            return null;

        return projectManagerPage;
    }
    @Override
    public List<ProjectManagerReadDTO> getAllDTOInPagingByProjectIdIn(Pageable paging, Collection<Long> projectIdCollection) throws Exception {
        Page<ProjectManager> projectManagerPage = getPageAllByProjectIdIn(paging, projectIdCollection);

        if (projectManagerPage == null) 
            return null;

        List<ProjectManager> projectManagerList = projectManagerPage.getContent();

        if (projectManagerList.isEmpty()) 
            return null;

        return fillAllDTO(projectManagerList, projectManagerPage.getTotalPages());
    }

    /* UPDATE */
    @Override
    public ProjectManager updateProjectManager(ProjectManager updatedProjectManager) throws Exception {
        ProjectManager oldProjectManager = getById(updatedProjectManager.getProjectManagerId());

        if (oldProjectManager == null) 
            return null;

        String errorMsg = "";

        /* Check input */
        if (updatedProjectManager.getRemoveDate() != null) {
            if (updatedProjectManager.getRemoveDate().isBefore(oldProjectManager.getAssignDate())) {
                errorMsg += "Invalid Input: removeDate is before assignDate. ";
            }
        }

        /* Check FK (if changed) */
        if (!oldProjectManager.getProjectId().equals(updatedProjectManager.getProjectId())) {
            if (!projectService.existsById(updatedProjectManager.getProjectId())) {
                errorMsg += "No Project found with Id: '" + updatedProjectManager.getProjectId()
                        + "'. Which violate constraint: FK_ProjectManager_Project. ";
            }
        }
        if (!oldProjectManager.getManagerId().equals(updatedProjectManager.getManagerId())) {
            if (!userService.existsById(updatedProjectManager.getManagerId())) {
                errorMsg += "No User (Manager) found with Id: '" + updatedProjectManager.getManagerId()
                        + "'. Which violate constraint: FK_ProjectManager_User_ManagerId. ";
            }
        }
        if (oldProjectManager.getUpdatedBy() != null) {
            if (!oldProjectManager.getUpdatedBy().equals(updatedProjectManager.getUpdatedBy())) {
                if (!userService.existsById(updatedProjectManager.getUpdatedBy())) {
                    errorMsg += "No User (UpdatedBy) found with Id: '" + updatedProjectManager.getUpdatedBy()
                            + "'. Which violate constraint: FK_ProjectManager_User_UpdatedBy. ";
                }
            }
        } else {
            if (!userService.existsById(updatedProjectManager.getUpdatedBy())) {
                errorMsg += "No User found with Id: '" + updatedProjectManager.getUpdatedBy()
                        + "'. Which violate constraint: FK_ProjectManager_User_UpdatedBy. ";
            }
        }

        /* Check duplicate */
        if (projectManagerRepository
                .existsByProjectIdAndManagerIdAndProjectManagerIdIsNotAndStatusNotIn(
                        updatedProjectManager.getProjectId(),
                        updatedProjectManager.getManagerId(),
                        updatedProjectManager.getProjectManagerId(),
                        N_D_S_STATUS_LIST)) {
            errorMsg += "Already exists another ProjectManager relationship between with Project with Id: '"
                    + updatedProjectManager.getProjectId()
                    + "' and User with Id: '"
                    + updatedProjectManager.getManagerId() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) 
            throw new IllegalArgumentException(errorMsg);

        return projectManagerRepository.saveAndFlush(updatedProjectManager);
    }
    @Override
    public ProjectManagerReadDTO updateProjectManagerByDTO(ProjectManagerUpdateDTO updatedProjectManagerDTO) throws Exception {
        modelMapper.typeMap(ProjectManagerUpdateDTO.class, ProjectManager.class)
                .addMappings(mapper -> {
                    mapper.skip(ProjectManager::setAssignDate);
                    mapper.skip(ProjectManager::setRemoveDate);});

        ProjectManager updatedProjectManager =
                modelMapper.map(updatedProjectManagerDTO, ProjectManager.class);

        boolean updateAssignDate = false;
        if (updatedProjectManagerDTO.getRemoveDate() != null) {
            updateAssignDate = true;

            updatedProjectManager.setAssignDate(
                    LocalDateTime.parse(updatedProjectManagerDTO.getAssignDate(), dateTimeFormatter));
        }

        if (updatedProjectManagerDTO.getRemoveDate() != null) {
            updatedProjectManager.setRemoveDate(
                    LocalDateTime.parse(updatedProjectManagerDTO.getRemoveDate(), dateTimeFormatter));

            if (updateAssignDate) {
                if (updatedProjectManager.getRemoveDate().isBefore(updatedProjectManager.getAssignDate())) {
                    throw new IllegalArgumentException("removeDate is before assignDate");
                }
            }
        }

        updatedProjectManager = updateProjectManager(updatedProjectManager);

        if (updatedProjectManager == null) 
            return null;

        return fillDTO(updatedProjectManager);
    }

    @Override
    public List<ProjectManager> updateBulkProjectManager(List<ProjectManager> updatedProjectManagerList) throws Exception {
        Set<Long> projectManagerIdSet = new HashSet<>();
        Map<Long, List<Long>> projectIdManagerIdListMap = new HashMap<>();

        Set<Long> updatedProjectIdSet = new HashSet<>();
        Set<Long> updatedManagerIdSet = new HashSet<>();
        Set<Long> updatedUpdatedBySet = new HashSet<>();

        Map<Long, ProjectManager> projectManagerIdUpdatedProjectManagerMap = new HashMap<>();

        List<Long> tmpManagerIdList;
        boolean isDuplicated = false;

        StringBuilder errorMsg = new StringBuilder();

        for (ProjectManager updatedProjectManager : updatedProjectManagerList) {
            projectManagerIdSet.add(updatedProjectManager.getProjectManagerId());
            updatedProjectIdSet.add(updatedProjectManager.getProjectId());
            updatedManagerIdSet.add(updatedProjectManager.getManagerId());
            updatedUpdatedBySet.add(updatedProjectManager.getManagerId());

            projectManagerIdUpdatedProjectManagerMap
                    .put(updatedProjectManager.getProjectManagerId(), updatedProjectManager);

            /* Check duplicate 1 (within input) */
            tmpManagerIdList = projectIdManagerIdListMap.get(updatedProjectManager.getProjectId());
            if (tmpManagerIdList == null) {
                projectIdManagerIdListMap.put(
                        updatedProjectManager.getProjectId(),
                        new ArrayList<>(Collections.singletonList(updatedProjectManager.getManagerId())));
            } else {
                if (tmpManagerIdList.contains(updatedProjectManager.getManagerId())) {
                    isDuplicated = true;
                    errorMsg.append("Duplicate ProjectManager relationship between with Project with Id: '")
                            .append(updatedProjectManager.getProjectId())
                            .append("' and User with Id: '")
                            .append(updatedProjectManager.getManagerId()).append("'. ");
                } else {
                    tmpManagerIdList.add(updatedProjectManager.getManagerId());
                    projectIdManagerIdListMap.put(updatedProjectManager.getProjectId(), tmpManagerIdList);
                }
            }
        }

        List<ProjectManager> oldProjectManagerList = getAllByIdIn(projectManagerIdSet);

        if (oldProjectManagerList == null) 
            return null;

        Set<Long> oldProjectIdSet = new HashSet<>();
        Set<Long> oldManagerIdSet = new HashSet<>();
        Set<Long> oldUpdatedBySet = new HashSet<>();

        Map<Long, Long> projectManagerIdCreatedByMap = new HashMap<>();
        Map<Long, LocalDateTime> projectManagerIdCreatedAtMap = new HashMap<>();

        for (ProjectManager oldProjectManager : oldProjectManagerList) {
            oldProjectIdSet.add(oldProjectManager.getProjectId());
            oldManagerIdSet.add(oldProjectManager.getManagerId());
            if (oldProjectManager.getUpdatedBy() != null) {
                oldUpdatedBySet.add(oldProjectManager.getManagerId());
            }

            projectManagerIdCreatedByMap.put(oldProjectManager.getProjectManagerId(), oldProjectManager.getCreatedBy());
            projectManagerIdCreatedAtMap.put(oldProjectManager.getProjectManagerId(), oldProjectManager.getCreatedAt());

            ProjectManager updatedProjectManager =
                    projectManagerIdUpdatedProjectManagerMap.get(oldProjectManager.getProjectManagerId());

            if (updatedProjectManager.getAssignDate() == null) {
                if (updatedProjectManager.getRemoveDate() != null){
                    if (updatedProjectManager.getRemoveDate().isBefore(oldProjectManager.getAssignDate())) {
                        errorMsg.append("Invalid Input: removeDate is before assignDate. ")
                                .append("At ProjectManager with id: '")
                                .append(oldProjectManager.getProjectManagerId())
                                .append("'. ");
                    }
                }

                updatedProjectManager.setAssignDate(oldProjectManager.getAssignDate());

                projectManagerIdUpdatedProjectManagerMap
                        .put(oldProjectManager.getProjectManagerId(), updatedProjectManager);
            }
        }

        /* Remove all unchanged projectId & managerId */
        updatedProjectIdSet.removeAll(oldProjectIdSet);
        updatedManagerIdSet.removeAll(oldManagerIdSet);
        updatedUpdatedBySet.removeAll(oldUpdatedBySet);

        /* Check FK (if changed) */
        /* If there are updated projectId, need to recheck FK */
        if (!updatedProjectIdSet.isEmpty()) {
            if (!projectService.existsAllByIdIn(updatedProjectIdSet)) {
                errorMsg.append("1 or more Project not found with Id. ")
                        .append("Which violate constraint: FK_ProjectManager_Project. ");
            }
        }
        /* If there are updated userId, need to recheck FK */
        if (!updatedManagerIdSet.isEmpty()) {
            if (!userService.existsAllByIdIn(updatedManagerIdSet)) {
                errorMsg.append("1 or more User not found with Id. ")
                        .append("Which violate constraint: FK_ProjectManager_User_ManagerId. ");
            }
        }
        if (!updatedUpdatedBySet.isEmpty()) {
            if (!userService.existsAllByIdIn(updatedUpdatedBySet)) {
                errorMsg.append("1 or more User not found with Id. ")
                        .append("Which violate constraint: FK_ProjectManager_User_UpdatedBy. ");
            }
        }

        /* Already has duplicated within input, no need to check with DB */
        if (!isDuplicated) {
            /* Check duplicate 2 (in DB) */
            for (ProjectManager updatedProjectManager : updatedProjectManagerList) {
                /* TODO: bulk check instead of loop */
                if (projectManagerRepository
                        .existsByProjectIdAndManagerIdAndProjectManagerIdIsNotAndStatusNotIn(
                                updatedProjectManager.getProjectId(),
                                updatedProjectManager.getManagerId(),
                                updatedProjectManager.getProjectManagerId(),
                                N_D_S_STATUS_LIST)) {
                    errorMsg.append("Already exists another ProjectManager relationship between Project with Id: '")
                            .append(updatedProjectManager.getProjectId())
                            .append("' and User with Id: '")
                            .append(updatedProjectManager.getManagerId()).append("'. ");
                }
            }
        }

        if (!errorMsg.toString().trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg.toString());
        }

        updatedProjectManagerList =
                updatedProjectManagerList.stream()
                        .peek(projectManager -> {
                            projectManager.setCreatedAt(
                                    projectManagerIdCreatedAtMap.get(projectManager.getProjectManagerId()));

                            projectManager.setCreatedBy(
                                    projectManagerIdCreatedByMap.get(projectManager.getProjectManagerId()));})
                        .collect(Collectors.toList());

        return projectManagerRepository.saveAllAndFlush(updatedProjectManagerList);
    }
    @Override
    public List<ProjectManagerReadDTO> updateBulkProjectManagerByDTO(List<ProjectManagerUpdateDTO> updatedProjectManagerDTOList) throws Exception {
        modelMapper.typeMap(ProjectManagerCreateDTO.class, ProjectManager.class)
                .addMappings(mapper -> {
                    mapper.skip(ProjectManager::setAssignDate);
                    mapper.skip(ProjectManager::setRemoveDate);});

        StringBuilder errorMsg = new StringBuilder();
        
        List<ProjectManager> projectManagerList =
                updatedProjectManagerDTOList.stream()
                        .map(projectManagerDTO -> {
                            ProjectManager projectManager =
                                    modelMapper.map(projectManagerDTO, ProjectManager.class);

                            boolean updateAssignDate = false;
                            if (projectManagerDTO.getRemoveDate() != null) {
                                updateAssignDate = true;

                                projectManager.setAssignDate(
                                        LocalDateTime.parse(projectManagerDTO.getAssignDate(), dateTimeFormatter));
                            }

                            if (projectManagerDTO.getRemoveDate() != null) {
                                projectManager.setRemoveDate(
                                        LocalDateTime.parse(projectManagerDTO.getRemoveDate(), dateTimeFormatter));

                                if (updateAssignDate) {
                                    if (projectManager.getRemoveDate().isBefore(projectManager.getAssignDate())) {
                                        errorMsg.append("Invalid input: removeDate is before assignDate. ")
                                                .append("At ProjectManager with id: '")
                                                .append(projectManager.getProjectManagerId())
                                                .append("'. ");
                                    }
                                }
                            }

                            return projectManager;})
                        .collect(Collectors.toList());

        if (!errorMsg.toString().trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg.toString());
        }

        projectManagerList = updateBulkProjectManager(projectManagerList);

        if (projectManagerList == null)
            return null;

        return fillAllDTO(projectManagerList, null);
    }

    /* DELETE */
    @Override
    public boolean removeProjectManager(long projectManagerId) throws Exception {
        ProjectManager projectManager = getById(projectManagerId);

        if (projectManager == null) 
            return false;

        projectManager.setStatus(Status.REMOVED);
        projectManagerRepository.saveAndFlush(projectManager);

        return true;
    }
    
    @Override
    public boolean removeAllByUserId(long userId) throws Exception {
        List<ProjectManager> projectManagerList = getAllByManagerId(userId);

        if (projectManagerList == null)
            return false;

        projectManagerList =
                projectManagerList.stream()
                        .peek(projectManager -> projectManager.setStatus(Status.REMOVED))
                        .collect(Collectors.toList());

        projectManagerRepository.saveAllAndFlush(projectManagerList);

        return true;
    }
    @Override
    public boolean removeAllByUserIdIn(Collection<Long> userIdCollection) throws Exception {
        List<ProjectManager> projectManagerList = getAllByManagerIdIn(userIdCollection);

        if (projectManagerList == null)
            return false;

        projectManagerList =
                projectManagerList.stream()
                        .peek(projectManager -> projectManager.setStatus(Status.REMOVED))
                        .collect(Collectors.toList());

        projectManagerRepository.saveAllAndFlush(projectManagerList);

        return true;
    }

    @Override
    public boolean removeAllByProjectId(long projectId) throws Exception {
        List<ProjectManager> projectManagerList = getAllByProjectId(projectId);

        if (projectManagerList == null)
            return false;

        projectManagerList =
                projectManagerList.stream()
                        .peek(projectManager -> projectManager.setStatus(Status.REMOVED))
                        .collect(Collectors.toList());

        projectManagerRepository.saveAllAndFlush(projectManagerList);

        return true;
    }
    @Override
    public boolean removeAllByProjectIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<ProjectManager> projectManagerList = getAllByProjectIdIn(projectIdCollection);

        if (projectManagerList == null)
            return false;

        projectManagerList =
                projectManagerList.stream()
                        .peek(projectManager -> projectManager.setStatus(Status.REMOVED))
                        .collect(Collectors.toList());

        projectManagerRepository.saveAllAndFlush(projectManagerList);

        return true;
    }

    @Override
    public boolean deleteProjectManager(long projectManagerId) throws Exception {
        ProjectManager projectManager = getById(projectManagerId);

        if (projectManager == null) 
            return false;

        projectManager.setStatus(Status.DELETED);
        projectManagerRepository.saveAndFlush(projectManager);

        return true;
    }

    @Override
    public boolean deleteAllByUserId(long userId) throws Exception {
        List<ProjectManager> projectManagerList = getAllByManagerId(userId);

        if (projectManagerList == null) 
            return false;

        projectManagerList =
                projectManagerList.stream()
                        .peek(projectManager -> projectManager.setStatus(Status.DELETED))
                        .collect(Collectors.toList());

        projectManagerRepository.saveAllAndFlush(projectManagerList);

        return true;
    }

    @Override
    public boolean deleteAllByProjectId(long projectId) throws Exception {
        List<ProjectManager> projectManagerList = getAllByProjectId(projectId);

        if (projectManagerList == null) 
            return false;

        projectManagerList =
                projectManagerList.stream()
                        .peek(projectManager -> projectManager.setStatus(Status.DELETED))
                        .collect(Collectors.toList());

        projectManagerRepository.saveAllAndFlush(projectManagerList);

        return true;
    }

    /* Utils */
    private ProjectManagerReadDTO fillDTO(ProjectManager projectManager) throws Exception {
        ProjectManagerReadDTO projectManagerDTO =
                modelMapper.map(projectManager, ProjectManagerReadDTO.class);

        /* Get associated User (Manager) */
        projectManagerDTO.setManager(userService.getDTOById(projectManager.getManagerId()));

        return projectManagerDTO;
    }

    private List<ProjectManagerReadDTO> fillAllDTO(Collection<ProjectManager> projectManagerCollection, Integer totalPage) throws Exception {
        Set<Long> managerIdSet =
                projectManagerCollection.stream()
                        .map(ProjectManager::getManagerId)
                        .collect(Collectors.toSet());

        /* Get associated User (Manager) */
        Map<Long, UserReadDTO> userIdUserDTOMap = userService.mapUserIdUserDTOByIdIn(managerIdSet);

        return projectManagerCollection.stream()
                .map(projectManager -> {
                    ProjectManagerReadDTO projectManagerDTO =
                            modelMapper.map(projectManager, ProjectManagerReadDTO.class);

                    projectManagerDTO.setManager(userIdUserDTOMap.get(projectManager.getManagerId()));

                    projectManagerDTO.setTotalPage(totalPage);

                    return projectManagerDTO;})
                .collect(Collectors.toList());
    }
}
