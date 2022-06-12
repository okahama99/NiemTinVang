package com.ntv.ntvcons_backend.services.project;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.entities.Project;
import com.ntv.ntvcons_backend.entities.projectModels.ShowProjectModel;
import com.ntv.ntvcons_backend.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService{
    @Autowired
    private ProjectRepository projectRepository;

    /* READ */
    @Override
    public Project createProject(String projectName, int locationId, Timestamp startDate, Timestamp endDate, int blueprintId, Double estimateCost) {
            Project tempForComment = new Project();
        //        if(userProfileRepository.findByUser(userRepository.getById(model.getUserID()))==null){
//            return null;
//        }else {
//            MarketPost marketPost = new MarketPost();
//            Config config = configRepository.getById(1);
//            if (config.isAutoBrowse()) {
//                marketPost.setStatus(Status.PENDING);
//            } else {
//                marketPost.setStatus(Status.ONSELL);
//            }
//            marketPost.setDescription(model.getDescription());
//            marketPost.setUser(userRepository.getById(model.getUserID()));
//            marketPost.setLocation(model.getLocation());
//            marketPost.setPrice(model.getPrice());
//            marketPost.setCreatedBy(model.getUserID());
//            marketPost.setTitle(model.getTitle());
//            marketPost.setQuantity(model.getQuantity());
//            marketPostRepository.saveAndFlush(marketPost);
//            for (MultipartFile file : files) { chỗ này là cho import file hình của project cũ
//                String fileName = imageService.save(file);
//                String imageUrl = imageService.getImageUrl(fileName);
//                MarketPostImage marketPostImage = new MarketPostImage();
//                marketPostImage.setMarketPost(marketPost);
//                marketPostImage.setImageName(imageUrl);
//                marketPostImageRepository.saveAndFlush(marketPostImage);
//            }
            return tempForComment;
//        }
    }

    /* READ */
    @Override
    public List<ShowProjectModel> getAll(int pageNo, int pageSize, String sortBy, boolean sortType) {
        Pageable paging;
        if(sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        }else{
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<Project> pagingResult = projectRepository.findAllByIsDeletedIsFalse(paging);

        if(pagingResult.hasContent()){
            double totalPage = Math.ceil((double)pagingResult.getTotalElements() / pageSize);

            Page<ShowProjectModel> modelResult =
                    pagingResult.map(new Converter<Project, ShowProjectModel>() {

                @Override
                protected ShowProjectModel doForward(Project project) {
                    ShowProjectModel model = new ShowProjectModel();

                    model.setLocationId(project.getLocationId());
                    model.setProjectId(project.getProjectId());
                    model.setBlueprintId(project.getBlueprintId());
                    model.setProjectName(project.getProjectName());
                    model.setStartDate(project.getPlanStartDate());
                    model.setEndDate(project.getPlanStartDate());
                    model.setEstimateCost(project.getEstimatedCost());
                    model.setCreatedAt(project.getCreatedAt());
                    model.setCreatedBy(project.getCreatedBy());
                    model.setUpdatedAt(project.getCreatedAt());
                    model.setUpdatedBy(project.getUpdatedBy());
                    model.setTotalPage(totalPage);

                    return model;
                }

                @Override
                protected Project doBackward(ShowProjectModel showProjectModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        }else{
            return new ArrayList<ShowProjectModel>();
        }
    }

    @Override
    public List<Project> getAllByIdIn(Collection<Integer> projectIdCollection) {
        return null;
    }

    @Override
    public List<Project> getAllByNameContains(String projectName) {
        return null;
    }

    @Override
    public List<Project> getAllByLocationIdIn(Collection<Integer> locationIdCollection) {
        return null;
    }

    @Override
    public List<Project> getAllByStartDateBetween(Timestamp from, Timestamp to) {
        return null;
    }

    @Override
    public List<Project> getAllByEndDateBetween(Timestamp from, Timestamp to) {
        return null;
    }

    @Override
    public List<Project> getAllByEstimateCostBetween(double from, double to) {
        return null;
    }

    @Override
    public Project getByLocationId(int locationId) {
        return null;
    }

    @Override
    public Project getByBlueprintId(int blueprintId) {
        return null;
    }

    @Override
    public Project getById(int projectId) {
        return null;
    }

    /* UPDATE */
    @Override
    public boolean updateProject(ShowProjectModel showProjectModel) {
        //MarketPost marketPost = marketPostRepository.findById(model.getMarketPostID()).get();
        //        if(marketPost != null)
        //        {
        //            marketPost.setDescription(model.getDescription());
        //            marketPost.setUser(userRepository.getById(model.getUserID()));
        //            marketPost.setLocation(model.getLocation());
        //            marketPost.setPrice(model.getPrice());
        //            marketPost.setCreatedBy(model.getUserID());
        //            marketPost.setTitle(model.getTitle());
        //            marketPost.setQuantity(model.getQuantity());
        //
        //            for (MarketPostImage image : marketPost.getMarketPostImages()) {
        //                String s = image.getImageName();
        //                MarketPostImage marketPostImage = marketPostImageRepository.findByImageName(s);
        //                marketPostImage.setMarketPost(null);
        //                marketPostImage.setImageName(null);
        //                marketPostImageRepository.saveAndFlush(marketPostImage);
        //                String[] strArr;
        //                strArr = s.split("[/;?]");
        //                imageService.delete(strArr[7]);
        //            }
        //
        //            for (MultipartFile file : files) {
        //                String fileName = imageService.save(file);
        //                String imageUrl = imageService.getImageUrl(fileName);
        //                MarketPostImage marketPostImage = new MarketPostImage();
        //                marketPostImage.setMarketPost(marketPost);
        //                marketPostImage.setImageName(imageUrl);
        //                marketPostImageRepository.saveAndFlush(marketPostImage);
        //            }
        //
        //            marketPostRepository.saveAndFlush(marketPost);
        //            return true;
        //        }
                return false;
    }

    /* DELETE */
    @Override
    public boolean deleteProject(int projectId) {
        //MarketPost marketPost = marketPostRepository.findById(postID).get();
        //        if(marketPost!=null)
        //        {
        //            marketPost.setStatus(Status.INACTIVE);
        //            marketPostRepository.saveAndFlush(marketPost);
        //            return true;
        //        }
                return false;
    }
}
