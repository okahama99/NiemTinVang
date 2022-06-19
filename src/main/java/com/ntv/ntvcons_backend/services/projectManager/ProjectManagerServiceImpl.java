package com.ntv.ntvcons_backend.services.projectManager;

import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerReadDTO;
import com.ntv.ntvcons_backend.dtos.projectManager.ProjectManagerUpdateDTO;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectManagerServiceImpl implements ProjectManagerService {
    @Autowired
    private ProjectManagerRepository projectManagerRepository;
    @Autowired
    private ModelMapper modelMapper;
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
            errorMsg += "No Project found with Id: " + newProjectManager.getProjectId()
                    + "Which violate constraint: FK_ProjectManager_Project. ";
        }

        if (!userService.existsById(newProjectManager.getManagerId())) {
            errorMsg += "No User found with Id: " + newProjectManager.getManagerId()
                    + "Which violate constraint: FK_ProjectManager_User. ";
        }

        /* Check duplicate */
        if (projectManagerRepository
                .existsByProjectIdAndManagerIdAndIsDeletedIsFalse(
                        newProjectManager.getProjectId(),
                        newProjectManager.getManagerId())) {
            errorMsg += "Already exists another ProjectManager relationship between with Project with Id: "
                    + newProjectManager.getProjectId()
                    + " and User with Id: "
                    + newProjectManager.getManagerId() + ". ";
        }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }

        return projectManagerRepository.saveAndFlush(newProjectManager);
    }

    @Override
    public List<ProjectManager> createBulkProjectManager(List<ProjectManager> newProjectManagerList) throws Exception {
        StringBuilder errorMsg = new StringBuilder();

        Map<Long, List<Long>> projectIdManagerIdMap = new HashMap<>();
        Set<Long> projectIdSet = new HashSet<>();
        Set<Long> managerIdSet = new HashSet<>();
        List<Long> tmpManagerIdList;
        boolean isDuplicated = false;

        for (ProjectManager newProjectManager : newProjectManagerList) {
            projectIdSet.add(newProjectManager.getProjectId());
            managerIdSet.add(newProjectManager.getManagerId());

            /* Check duplicate 1 (within input) */
            tmpManagerIdList = projectIdManagerIdMap.get(newProjectManager.getProjectId());
            if (tmpManagerIdList == null) {
                projectIdManagerIdMap.put(
                        newProjectManager.getProjectId(),
                        new ArrayList<>(Collections.singletonList(newProjectManager.getManagerId())));
            } else {
                if (tmpManagerIdList.contains(newProjectManager.getManagerId())) {
                    isDuplicated = true;
                    errorMsg.append("Duplicate ProjectManager relationship between with Project with Id: ")
                            .append(newProjectManager.getProjectId())
                            .append(" and User with Id: ")
                            .append(newProjectManager.getManagerId()).append(". ");
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
            errorMsg.append("1 or more User not found with Id. Which violate constraint: FK_ProjectManager_User. ");
        }

        /* Already has duplicated within input, no need to check with DB */
        if (!isDuplicated) {
            /* Check duplicate 2 (in DB) */
            for (ProjectManager newProjectManager : newProjectManagerList) {
                /* TODO: bulk check instead of loop */
                if (projectManagerRepository
                                .existsByProjectIdAndManagerIdAndIsDeletedIsFalse(
                                        newProjectManager.getProjectId(),
                                        newProjectManager.getManagerId())) {
                    errorMsg.append("Already exists another ProjectManager relationship between with Project with Id: ")
                            .append(newProjectManager.getProjectId())
                            .append(" and User with Id: ")
                            .append(newProjectManager.getManagerId()).append(". ");
                }
            }
        }

        if (!errorMsg.toString().trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg.toString());
        }

        return projectManagerRepository.saveAllAndFlush(newProjectManagerList);
    }

    /* READ */
    @Override
    public List<ProjectManager> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) throws Exception {
        Pageable paging;
        if (sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<ProjectManager> projectManagerPage = projectManagerRepository.findAllByIsDeletedIsFalse(paging);

        if (projectManagerPage.isEmpty()) {
            return null;
        }

        return projectManagerPage.getContent();
    }

    @Override
    public ProjectManager getById(long projectManagerId) throws Exception {
        return projectManagerRepository
                .findByProjectManagerIdAndIsDeletedIsFalse(projectManagerId)
                .orElse(null);
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
    public List<ProjectManager> getAllByManagerId(long managerId) throws Exception {
        List<ProjectManager> projectManagerList =
                projectManagerRepository.findAllByManagerIdAndIsDeletedIsFalse(managerId);

        if (projectManagerList.isEmpty()) {
            return null;
        }

        return projectManagerList;
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
                errorMsg += "No Project found with Id: " + updatedProjectManager.getProjectId()
                        + "Which violate constraint: FK_ProjectManager_Project. ";
            }
        }

        if (!oldProjectManager.getManagerId().equals(updatedProjectManager.getManagerId())) {
            if (!userService.existsById(updatedProjectManager.getManagerId())) {
                errorMsg += "No User found with Id: " + updatedProjectManager.getManagerId()
                        + "Which violate constraint: FK_ProjectManager_User. ";
            }
        }

        /* Check duplicate */
        if (projectManagerRepository
                .existsByProjectIdAndManagerIdAndProjectManagerIdIsNotAndIsDeletedIsFalse(
                        updatedProjectManager.getProjectId(),
                        updatedProjectManager.getManagerId(),
                        updatedProjectManager.getProjectManagerId())) {
            errorMsg += "Already exists another ProjectManager relationship between with Project with Id: "
                    + updatedProjectManager.getProjectId()
                    + " and User with Id: "
                    + updatedProjectManager.getManagerId() + ". ";
        }

        if (!errorMsg.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }

        return projectManagerRepository.saveAndFlush(updatedProjectManager);
    }
    @Override
    public ProjectManagerReadDTO updateProjectManagerByDTO(ProjectManagerUpdateDTO updatedProjectManagerDTO) throws Exception {
        ProjectManager updatedProjectManager =
                modelMapper.map(updatedProjectManagerDTO, ProjectManager.class);

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

    public List<ProjectManager> updateBulkProjectManager(List<ProjectManager> updatedProjectManagerList) throws Exception {
        Set<Long> projectManagerIdSet = new HashSet<>();
        Map<Long, List<Long>> projectIdManagerIdMap = new HashMap<>();
        Set<Long> oldProjectIdSet = new HashSet<>();
        Set<Long> oldManagerIdSet = new HashSet<>();
        Set<Long> updatedProjectIdSet = new HashSet<>();
        Set<Long> updatedManagerIdSet = new HashSet<>();
        List<Long> tmpManagerIdList;
        boolean isDuplicated = false;

        StringBuilder errorMsg = new StringBuilder();

        for (ProjectManager updatedProjectManager : updatedProjectManagerList) {
            projectManagerIdSet.add(updatedProjectManager.getProjectManagerId());
            updatedProjectIdSet.add(updatedProjectManager.getProjectId());
            updatedManagerIdSet.add(updatedProjectManager.getManagerId());

            /* Check duplicate 1 (within input) */
            tmpManagerIdList = projectIdManagerIdMap.get(updatedProjectManager.getProjectId());
            if (tmpManagerIdList == null) {
                projectIdManagerIdMap.put(
                        updatedProjectManager.getProjectId(),
                        new ArrayList<>(Collections.singletonList(updatedProjectManager.getManagerId())));
            } else {
                if (tmpManagerIdList.contains(updatedProjectManager.getManagerId())) {
                    isDuplicated = true;
                    errorMsg.append("Duplicate ProjectManager relationship between with Project with Id: ")
                            .append(updatedProjectManager.getProjectId())
                            .append(" and User with Id: ")
                            .append(updatedProjectManager.getManagerId()).append(". ");
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
        }

        /* Remove all unchanged reportId & taskId */
        updatedProjectIdSet.removeAll(oldProjectIdSet);
        updatedManagerIdSet.removeAll(oldManagerIdSet);

        /* Check FK */
        /* If there are updated projectId, need to recheck FK */
        if (!updatedProjectIdSet.isEmpty()) {
            if (!projectService.existsAllByIdIn(updatedProjectIdSet)) {
                errorMsg.append("1 or more Project not found with Id. Which violate constraint: FK_ProjectManager_Project. ");
            }
        }

        /* If there are updated userId, need to recheck FK */
        if (!updatedManagerIdSet.isEmpty()) {
            if (!userService.existsAllByIdIn(updatedManagerIdSet)) {
                errorMsg.append("1 or more User not found with Id. Which violate constraint: FK_ProjectManager_User. ");
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
                    errorMsg.append("Already exists another ProjectManager relationship between with Project with Id: ")
                            .append(updatedProjectManager.getProjectId())
                            .append(" and User with Id: ")
                            .append(updatedProjectManager.getManagerId()).append(". ");
                }
            }
        }

        if (!errorMsg.toString().trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg.toString());
        }

        return projectManagerRepository.saveAllAndFlush(updatedProjectManagerList);
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
