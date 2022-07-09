package com.ntv.ntvcons_backend.services.projectManager;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                .existsByProjectIdAndManagerIdAndIsDeletedIsFalse(
                        newProjectManager.getProjectId(),
                        newProjectManager.getManagerId())) {
            errorMsg += "Already exists another ProjectManager relationship between with Project with Id: '"
                    + newProjectManager.getProjectId()
                    + "' and User with Id: '"
                    + newProjectManager.getManagerId() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }

        return projectManagerRepository.saveAndFlush(newProjectManager);
    }
    @Override
    public ProjectManagerReadDTO createProjectManagerByDTO(ProjectManagerCreateDTO newProjectManagerDTO) throws Exception {
        modelMapper.typeMap(ProjectManagerCreateDTO.class, ProjectManager.class)
                .addMappings(mapper -> {
                    mapper.skip(ProjectManager::setAssignDate);});

        ProjectManager newProjectManager = modelMapper.map(newProjectManagerDTO, ProjectManager.class);

        newProjectManager.setAssignDate(
                LocalDateTime.parse(newProjectManagerDTO.getAssignDate(), dateTimeFormatter));

        newProjectManager = createProjectManager(newProjectManager);

        ProjectManagerReadDTO projectManagerDTO = modelMapper.map(newProjectManager, ProjectManagerReadDTO.class);

        projectManagerDTO.setManager(userService.getDTOById(newProjectManager.getManagerId()));

        return projectManagerDTO;
    }

    @Override
    public List<ProjectManager> createBulkProjectManager(List<ProjectManager> newProjectManagerList) throws Exception {
        StringBuilder errorMsg = new StringBuilder();

        Map<Long, List<Long>> projectIdManagerIdMap = new HashMap<>();
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
            tmpManagerIdList = projectIdManagerIdMap.get(newProjectManager.getProjectId());

            if (tmpManagerIdList == null) {
                projectIdManagerIdMap.put(
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
                       projectIdManagerIdMap.put(newProjectManager.getProjectId(), tmpManagerIdList);
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
                                .existsByProjectIdAndManagerIdAndIsDeletedIsFalse(
                                        newProjectManager.getProjectId(),
                                        newProjectManager.getManagerId())) {
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
        modelMapper.typeMap(ProjectManagerCreateDTO.class, ProjectManager.class)
                .addMappings(mapper -> {
                    mapper.skip(ProjectManager::setAssignDate);});

        List<ProjectManager> projectManagerList =
                newProjectManagerDTOList.stream()
                        .map(projectManagerDTO -> {
                            ProjectManager projectManager =
                                    modelMapper.map(projectManagerDTO, ProjectManager.class);

                            projectManager.setAssignDate(
                                    LocalDateTime.parse(projectManagerDTO.getAssignDate(), dateTimeFormatter));

                            return projectManager;})
                        .collect(Collectors.toList());

        projectManagerList = createBulkProjectManager(projectManagerList);

        Map<Long, UserReadDTO> userIdUserDTOMap =
                userService.mapUserIdUserDTOByIdIn(
                        projectManagerList.stream()
                                .map(ProjectManager::getManagerId)
                                .collect(Collectors.toSet()));

        return projectManagerList.stream()
                .map(projectManager -> {
                    ProjectManagerReadDTO projectManagerDTO =
                            modelMapper.map(projectManager, ProjectManagerReadDTO.class);

                    projectManagerDTO.setManager(userIdUserDTOMap.get(projectManager.getManagerId()));

                    return projectManagerDTO;})
                .collect(Collectors.toList());
    }

    /* READ */
    @Override
    public Page<ProjectManager> getPageAll(Pageable paging) throws Exception {
        Page<ProjectManager> projectManagerPage = projectManagerRepository.findAllByIsDeletedIsFalse(paging);

        if (projectManagerPage.isEmpty()) {
            return null;
        }

        return projectManagerPage;
    }
    @Override
    public List<ProjectManagerReadDTO> getAllDTOInPaging(Pageable paging) throws Exception {
        Page<ProjectManager> projectManagerPage = getPageAll(paging);

        if (projectManagerPage == null) {
            return null;
        }

        List<ProjectManager> projectManagerList = projectManagerPage.getContent();

        if (projectManagerList.isEmpty()) {
            return null;
        }

        int totalPage = projectManagerPage.getTotalPages();

        Map<Long, UserReadDTO> userIdUserDTOMap =
                userService.mapUserIdUserDTOByIdIn(
                        projectManagerList.stream()
                                .map(ProjectManager::getManagerId)
                                .collect(Collectors.toSet()));

        return projectManagerList.stream()
                .map(projectManager -> {
                    ProjectManagerReadDTO projectManagerDTO =
                            modelMapper.map(projectManager, ProjectManagerReadDTO.class);

                    projectManagerDTO.setManager(userIdUserDTOMap.get(projectManager.getManagerId()));

                    projectManagerDTO.setTotalPage(totalPage);

                    return projectManagerDTO;})
                .collect(Collectors.toList());
    }

    @Override
    public ProjectManager getById(long projectManagerId) throws Exception {
        return projectManagerRepository
                .findByProjectManagerIdAndIsDeletedIsFalse(projectManagerId)
                .orElse(null);
    }
    @Override
    public ProjectManagerReadDTO getDTOById(long projectManagerId) throws Exception {
        ProjectManager projectManager = getById(projectManagerId);

        if (projectManager == null) {
            return null;
        }

        ProjectManagerReadDTO projectManagerDTO = modelMapper.map(projectManager, ProjectManagerReadDTO.class);

        projectManagerDTO.setManager(userService.getDTOById(projectManager.getManagerId()));

        return projectManagerDTO;
    }

    @Override
    public List<ProjectManager> getAllByIdIn(Collection<Long> projectManagerIdCollection) throws Exception {
        List<ProjectManager> projectManagerList =
                projectManagerRepository.findAllByProjectManagerIdInAndIsDeletedIsFalse(projectManagerIdCollection);

        if (projectManagerList.isEmpty()) {
            return null;
        }

        return projectManagerList;
    }
    @Override
    public List<ProjectManagerReadDTO> getAllDTOByIdIn(Collection<Long> projectManagerIdCollection) throws Exception {
        List<ProjectManager> projectManagerList = getAllByIdIn(projectManagerIdCollection);

        if (projectManagerList == null) {
            return null;
        }

        Map<Long, UserReadDTO> userIdUserDTOMap =
                userService.mapUserIdUserDTOByIdIn(
                        projectManagerList.stream()
                                .map(ProjectManager::getManagerId)
                                .collect(Collectors.toSet()));

        return projectManagerList.stream()
                .map(projectManager -> {
                    ProjectManagerReadDTO projectManagerDTO =
                            modelMapper.map(projectManager, ProjectManagerReadDTO.class);

                    projectManagerDTO.setManager(userIdUserDTOMap.get(projectManager.getManagerId()));

                    return projectManagerDTO;})
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectManager> getAllByManagerId(long managerId) throws Exception {
        List<ProjectManager> projectManagerList =
                projectManagerRepository.findAllByManagerIdAndIsDeletedIsFalse(managerId);

        if (projectManagerList.isEmpty()) {
            return null;
        }

        return projectManagerList;
    }
    @Override
    public List<ProjectManagerReadDTO> getAllDTOByManagerId(long managerId) throws Exception {
        List<ProjectManager> projectManagerList = getAllByManagerId(managerId);

        if (projectManagerList == null) {
            return null;
        }

        Map<Long, UserReadDTO> userIdUserDTOMap =
                userService.mapUserIdUserDTOByIdIn(
                        projectManagerList.stream()
                                .map(ProjectManager::getManagerId)
                                .collect(Collectors.toSet()));

        return projectManagerList.stream()
                .map(projectManager -> {
                    ProjectManagerReadDTO projectManagerDTO =
                            modelMapper.map(projectManager, ProjectManagerReadDTO.class);

                    projectManagerDTO.setManager(userIdUserDTOMap.get(projectManager.getManagerId()));

                    return projectManagerDTO;})
                .collect(Collectors.toList());
    }
    @Override
    public Page<ProjectManager> getPageAllByManagerId(Pageable paging, long managerId) throws Exception {
        Page<ProjectManager> projectManagerPage =
                projectManagerRepository.findAllByManagerIdAndIsDeletedIsFalse(managerId, paging);

        if (projectManagerPage.isEmpty()) {
            return null;
        }

        return projectManagerPage;
    }
    @Override
    public List<ProjectManagerReadDTO> getAllDTOInPagingByManagerId(Pageable paging, long managerId) throws Exception {
        Page<ProjectManager> projectManagerPage = getPageAllByManagerId(paging, managerId);

        if (projectManagerPage == null) {
            return null;
        }

        List<ProjectManager> projectManagerList = projectManagerPage.getContent();

        if (projectManagerList.isEmpty()) {
            return null;
        }

        int totalPage = projectManagerPage.getTotalPages();

        Map<Long, UserReadDTO> userIdUserDTOMap =
                userService.mapUserIdUserDTOByIdIn(
                        projectManagerList.stream()
                                .map(ProjectManager::getManagerId)
                                .collect(Collectors.toSet()));

        return projectManagerList.stream()
                .map(projectManager -> {
                    ProjectManagerReadDTO projectManagerDTO =
                            modelMapper.map(projectManager, ProjectManagerReadDTO.class);

                    projectManagerDTO.setManager(userIdUserDTOMap.get(projectManager.getManagerId()));

                    projectManagerDTO.setTotalPage(totalPage);

                    return projectManagerDTO;})
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectManager> getAllByManagerIdIn(Collection<Long> managerIdCollection) throws Exception {
        List<ProjectManager> projectManagerList =
                projectManagerRepository.findAllByManagerIdInAndIsDeletedIsFalse(managerIdCollection);

        if (projectManagerList.isEmpty()) {
            return null;
        }

        return projectManagerList;
    }
    @Override
    public List<ProjectManagerReadDTO> getAllDTOByManagerIdIn(Collection<Long> managerIdCollection) throws Exception {
        List<ProjectManager> projectManagerList = getAllByManagerIdIn(managerIdCollection);

        if (projectManagerList == null) {
            return null;
        }

        Map<Long, UserReadDTO> userIdUserDTOMap =
                userService.mapUserIdUserDTOByIdIn(
                        projectManagerList.stream()
                                .map(ProjectManager::getManagerId)
                                .collect(Collectors.toSet()));

        return projectManagerList.stream()
                .map(projectManager -> {
                    ProjectManagerReadDTO projectManagerDTO =
                            modelMapper.map(projectManager, ProjectManagerReadDTO.class);

                    projectManagerDTO.setManager(userIdUserDTOMap.get(projectManager.getManagerId()));

                    return projectManagerDTO;})
                .collect(Collectors.toList());
    }
    @Override
    public Page<ProjectManager> getPageAllByManagerIdIn(Pageable paging, Collection<Long> managerIdCollection) throws Exception {
        Page<ProjectManager> projectManagerPage =
                projectManagerRepository.findAllByManagerIdInAndIsDeletedIsFalse(managerIdCollection, paging);

        if (projectManagerPage.isEmpty()) {
            return null;
        }

        return projectManagerPage;
    }
    @Override
    public List<ProjectManagerReadDTO> getAllDTOInPagingByManagerIdIn(Pageable paging, Collection<Long> managerIdCollection) throws Exception {
        Page<ProjectManager> projectManagerPage = getPageAllByManagerIdIn(paging, managerIdCollection);

        if (projectManagerPage == null) {
            return null;
        }

        List<ProjectManager> projectManagerList = projectManagerPage.getContent();

        if (projectManagerList.isEmpty()) {
            return null;
        }

        int totalPage = projectManagerPage.getTotalPages();

        Map<Long, UserReadDTO> userIdUserDTOMap =
                userService.mapUserIdUserDTOByIdIn(
                        projectManagerList.stream()
                                .map(ProjectManager::getManagerId)
                                .collect(Collectors.toSet()));

        return projectManagerList.stream()
                .map(projectManager -> {
                    ProjectManagerReadDTO projectManagerDTO =
                            modelMapper.map(projectManager, ProjectManagerReadDTO.class);

                    projectManagerDTO.setManager(userIdUserDTOMap.get(projectManager.getManagerId()));

                    projectManagerDTO.setTotalPage(totalPage);

                    return projectManagerDTO;})
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectManager> getAllByProjectId(long projectId) throws Exception {
        List<ProjectManager> projectManagerList =
                projectManagerRepository.findAllByProjectIdAndIsDeletedIsFalse(projectId);

        if (projectManagerList.isEmpty()) {
            return null;
        }

        return projectManagerList;
    }
    @Override
    public List<ProjectManagerReadDTO> getAllDTOByProjectId(long projectId) throws Exception {
        List<ProjectManager> projectManagerList = getAllByProjectId(projectId);

        if (projectManagerList == null) {
            return null;
        }

        Map<Long, UserReadDTO> userIdUserDTOMap =
                userService.mapUserIdUserDTOByIdIn(
                        projectManagerList.stream()
                                .map(ProjectManager::getManagerId)
                                .collect(Collectors.toSet()));

        return projectManagerList.stream()
                .map(projectManager -> {
                    ProjectManagerReadDTO projectManagerDTO =
                            modelMapper.map(projectManager, ProjectManagerReadDTO.class);

                    projectManagerDTO.setManager(userIdUserDTOMap.get(projectManager.getManagerId()));

                    return projectManagerDTO;})
                .collect(Collectors.toList());
    }
    @Override
    public Page<ProjectManager> getPageAllByProjectId(Pageable paging, long projectId) throws Exception {
        Page<ProjectManager> projectManagerPage =
                projectManagerRepository.findAllByProjectIdAndIsDeletedIsFalse(projectId, paging);

        if (projectManagerPage.isEmpty()) {
            return null;
        }

        return projectManagerPage;
    }
    @Override
    public List<ProjectManagerReadDTO> getAllDTOInPagingByProjectId(Pageable paging, long projectId) throws Exception {
        Page<ProjectManager> projectManagerPage = getPageAllByProjectId(paging, projectId);

        if (projectManagerPage == null) {
            return null;
        }

        List<ProjectManager> projectManagerList = projectManagerPage.getContent();

        if (projectManagerList.isEmpty()) {
            return null;
        }

        int totalPage = projectManagerPage.getTotalPages();

        Map<Long, UserReadDTO> userIdUserDTOMap =
                userService.mapUserIdUserDTOByIdIn(
                        projectManagerList.stream()
                                .map(ProjectManager::getManagerId)
                                .collect(Collectors.toSet()));

        return projectManagerList.stream()
                .map(projectManager -> {
                    ProjectManagerReadDTO projectManagerDTO =
                            modelMapper.map(projectManager, ProjectManagerReadDTO.class);

                    projectManagerDTO.setManager(userIdUserDTOMap.get(projectManager.getManagerId()));

                    projectManagerDTO.setTotalPage(totalPage);

                    return projectManagerDTO;})
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectManager> getAllByProjectIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<ProjectManager> projectManagerList =
                projectManagerRepository.findAllByProjectIdInAndIsDeletedIsFalse(projectIdCollection);

        if (projectManagerList.isEmpty()) {
            return null;
        }

        return projectManagerList;
    }
    @Override
    public List<ProjectManagerReadDTO> getAllDTOByProjectIdIn(Collection<Long> projectIdCollection) throws Exception {
        List<ProjectManager> projectManagerList = getAllByProjectIdIn(projectIdCollection);

        if (projectManagerList == null) {
            return null;
        }

        Map<Long, UserReadDTO> userIdUserDTOMap =
                userService.mapUserIdUserDTOByIdIn(
                        projectManagerList.stream()
                                .map(ProjectManager::getManagerId)
                                .collect(Collectors.toSet()));

        return projectManagerList.stream()
                .map(projectManager -> {
                    ProjectManagerReadDTO projectManagerDTO =
                            modelMapper.map(projectManager, ProjectManagerReadDTO.class);

                    projectManagerDTO.setManager(userIdUserDTOMap.get(projectManager.getManagerId()));

                    return projectManagerDTO;})
                .collect(Collectors.toList());
    }
    @Override
    public Page<ProjectManager> getPageAllByProjectIdIn(Pageable paging, Collection<Long> projectIdCollection) throws Exception {
        Page<ProjectManager> projectManagerPage =
                projectManagerRepository.findAllByProjectIdInAndIsDeletedIsFalse(projectIdCollection, paging);

        if (projectManagerPage.isEmpty()) {
            return null;
        }

        return projectManagerPage;
    }
    @Override
    public List<ProjectManagerReadDTO> getAllDTOInPagingByProjectIdIn(Pageable paging, Collection<Long> projectIdCollection) throws Exception {
        Page<ProjectManager> projectManagerPage = getPageAllByProjectIdIn(paging, projectIdCollection);

        if (projectManagerPage == null) {
            return null;
        }

        List<ProjectManager> projectManagerList = projectManagerPage.getContent();

        if (projectManagerList.isEmpty()) {
            return null;
        }

        int totalPage = projectManagerPage.getTotalPages();

        Map<Long, UserReadDTO> userIdUserDTOMap =
                userService.mapUserIdUserDTOByIdIn(
                        projectManagerList.stream()
                                .map(ProjectManager::getManagerId)
                                .collect(Collectors.toSet()));

        return projectManagerList.stream()
                .map(projectManager -> {
                    ProjectManagerReadDTO projectManagerDTO =
                            modelMapper.map(projectManager, ProjectManagerReadDTO.class);

                    projectManagerDTO.setManager(userIdUserDTOMap.get(projectManager.getManagerId()));

                    projectManagerDTO.setTotalPage(totalPage);

                    return projectManagerDTO;})
                .collect(Collectors.toList());
    }

    /* UPDATE */
    @Override
    public ProjectManager updateProjectManager(ProjectManager updatedProjectManager) throws Exception {
        ProjectManager oldProjectManager = getById(updatedProjectManager.getProjectManagerId());

        if(oldProjectManager == null) {
            return null;
        }

        String errorMsg = "";

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
                .existsByProjectIdAndManagerIdAndProjectManagerIdIsNotAndIsDeletedIsFalse(
                        updatedProjectManager.getProjectId(),
                        updatedProjectManager.getManagerId(),
                        updatedProjectManager.getProjectManagerId())) {
            errorMsg += "Already exists another ProjectManager relationship between with Project with Id: '"
                    + updatedProjectManager.getProjectId()
                    + "' and User with Id: '"
                    + updatedProjectManager.getManagerId() + "'. ";
        }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }

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

        updatedProjectManager.setAssignDate(
                LocalDateTime.parse(updatedProjectManagerDTO.getAssignDate(), dateTimeFormatter));

        if (updatedProjectManagerDTO.getRemoveDate() != null) {
            updatedProjectManager.setRemoveDate(
                    LocalDateTime.parse(updatedProjectManagerDTO.getRemoveDate(), dateTimeFormatter));

            if (updatedProjectManager.getRemoveDate().isBefore(updatedProjectManager.getAssignDate())) {
                throw new IllegalArgumentException("removeDate is before assignDate");
            }
        }

        updatedProjectManager = updateProjectManager(updatedProjectManager);

        if (updatedProjectManager == null) {
            return null;
        }

        ProjectManagerReadDTO projectManagerDTO =
                modelMapper.map(updatedProjectManager, ProjectManagerReadDTO.class);

        /* Get associated User */
        projectManagerDTO.setManager(userService.getDTOById(updatedProjectManager.getManagerId()));

        return projectManagerDTO;
    }

    @Override
    public List<ProjectManager> updateBulkProjectManager(List<ProjectManager> updatedProjectManagerList) throws Exception {
        Set<Long> projectManagerIdSet = new HashSet<>();
        Map<Long, List<Long>> projectIdManagerIdMap = new HashMap<>();
        Set<Long> oldProjectIdSet = new HashSet<>();
        Set<Long> oldManagerIdSet = new HashSet<>();
        Set<Long> oldUpdatedBySet = new HashSet<>();
        Set<Long> updatedProjectIdSet = new HashSet<>();
        Set<Long> updatedManagerIdSet = new HashSet<>();
        Set<Long> updatedUpdatedBySet = new HashSet<>();
        List<Long> tmpManagerIdList;
        boolean isDuplicated = false;

        StringBuilder errorMsg = new StringBuilder();

        for (ProjectManager updatedProjectManager : updatedProjectManagerList) {
            projectManagerIdSet.add(updatedProjectManager.getProjectManagerId());
            updatedProjectIdSet.add(updatedProjectManager.getProjectId());
            updatedManagerIdSet.add(updatedProjectManager.getManagerId());
            updatedUpdatedBySet.add(updatedProjectManager.getManagerId());

            /* Check duplicate 1 (within input) */
            tmpManagerIdList = projectIdManagerIdMap.get(updatedProjectManager.getProjectId());
            if (tmpManagerIdList == null) {
                projectIdManagerIdMap.put(
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
                    projectIdManagerIdMap.put(updatedProjectManager.getProjectId(), tmpManagerIdList);
                }
            }
        }

        List<ProjectManager> oldProjectManagerList = getAllByIdIn(projectManagerIdSet);

        if (oldProjectManagerList == null) {
            return null;
        }

        for (ProjectManager oldProjectManager : oldProjectManagerList) {
            oldProjectIdSet.add(oldProjectManager.getProjectId());
            oldManagerIdSet.add(oldProjectManager.getManagerId());
            if (oldProjectManager.getUpdatedBy() != null) {
                oldUpdatedBySet.add(oldProjectManager.getManagerId());
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
                errorMsg.append("1 or more Project not found with Id. Which violate constraint: FK_ProjectManager_Project. ");
            }
        }
        /* If there are updated userId, need to recheck FK */
        if (!updatedManagerIdSet.isEmpty()) {
            if (!userService.existsAllByIdIn(updatedManagerIdSet)) {
                errorMsg.append("1 or more User not found with Id. Which violate constraint: FK_ProjectManager_User_ManagerId. ");
            }
        }
        if (!updatedUpdatedBySet.isEmpty()) {
            if (!userService.existsAllByIdIn(updatedUpdatedBySet)) {
                errorMsg.append("1 or more User not found with Id. Which violate constraint: FK_ProjectManager_User_UpdatedBy. ");
            }
        }

        /* Already has duplicated within input, no need to check with DB */
        if (!isDuplicated) {
            /* Check duplicate 2 (in DB) */
            for (ProjectManager updatedProjectManager : updatedProjectManagerList) {
                /* TODO: bulk check instead of loop */
                if (projectManagerRepository
                        .existsByProjectIdAndManagerIdAndProjectManagerIdIsNotAndIsDeletedIsFalse(
                                updatedProjectManager.getProjectId(),
                                updatedProjectManager.getManagerId(),
                                updatedProjectManager.getProjectManagerId())) {
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

        return projectManagerRepository.saveAllAndFlush(updatedProjectManagerList);
    }
    @Override
    public List<ProjectManagerReadDTO> updateBulkProjectManagerByDTO(List<ProjectManagerUpdateDTO> updatedProjectManagerDTOList) throws Exception {
        modelMapper.typeMap(ProjectManagerCreateDTO.class, ProjectManager.class)
                .addMappings(mapper -> {
                    mapper.skip(ProjectManager::setAssignDate);
                    mapper.skip(ProjectManager::setRemoveDate);});

        List<ProjectManager> projectManagerList =
                updatedProjectManagerDTOList.stream()
                        .map(projectManagerDTO -> {
                            ProjectManager projectManager =
                                    modelMapper.map(projectManagerDTO, ProjectManager.class);

                            projectManager.setAssignDate(
                                    LocalDateTime.parse(projectManagerDTO.getAssignDate(), dateTimeFormatter));

                            if (projectManagerDTO.getRemoveDate() != null) {
                                projectManager.setRemoveDate(
                                        LocalDateTime.parse(projectManagerDTO.getRemoveDate(), dateTimeFormatter));

                                if (projectManager.getRemoveDate().isBefore(projectManager.getAssignDate())) {
                                    throw new IllegalArgumentException("removeDate is before assignDate");
                                }
                            }

                            return projectManager;})
                        .collect(Collectors.toList());

        projectManagerList = updateBulkProjectManager(projectManagerList);

        Map<Long, UserReadDTO> userIdUserDTOMap =
                userService.mapUserIdUserDTOByIdIn(
                        projectManagerList.stream()
                                .map(ProjectManager::getManagerId)
                                .collect(Collectors.toSet()));

        return projectManagerList.stream()
                .map(projectManager -> {
                    ProjectManagerReadDTO projectManagerDTO =
                            modelMapper.map(projectManager, ProjectManagerReadDTO.class);

                    projectManagerDTO.setManager(userIdUserDTOMap.get(projectManager.getManagerId()));

                    return projectManagerDTO;})
                .collect(Collectors.toList());
    }

    /* DELETE */
    @Override
    public boolean deleteProjectManager(long projectManagerId) throws Exception {
        ProjectManager projectManager = getById(projectManagerId);

        if(projectManager == null) {
            return false;
        }

        projectManager.setIsDeleted(true);
        projectManagerRepository.saveAndFlush(projectManager);

        return true;
    }

    @Override
    public boolean deleteAllByUserId(long userId) throws Exception {
        List<ProjectManager> projectManagerList = getAllByManagerId(userId);

        if(projectManagerList == null) {
            return false;
        }

        projectManagerList =
                projectManagerList.stream()
                        .peek(projectManager -> projectManager.setIsDeleted(true))
                        .collect(Collectors.toList());

        projectManagerRepository.saveAllAndFlush(projectManagerList);

        return true;
    }

    @Override
    public boolean deleteAllByProjectId(long projectId) throws Exception {
        List<ProjectManager> projectManagerList = getAllByProjectId(projectId);

        if(projectManagerList == null) {
            return false;
        }

        projectManagerList =
                projectManagerList.stream()
                        .peek(projectManager -> projectManager.setIsDeleted(true))
                        .collect(Collectors.toList());

        projectManagerRepository.saveAllAndFlush(projectManagerList);

        return true;
    }
}
