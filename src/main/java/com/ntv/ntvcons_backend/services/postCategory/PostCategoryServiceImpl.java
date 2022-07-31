package com.ntv.ntvcons_backend.services.postCategory;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.entities.PostCategory;
import com.ntv.ntvcons_backend.entities.PostCategoryModels.CreatePostCategoryModel;
import com.ntv.ntvcons_backend.entities.PostCategoryModels.ShowPostCategoryModel;
import com.ntv.ntvcons_backend.entities.PostCategoryModels.UpdatePostCategoryModel;
import com.ntv.ntvcons_backend.repositories.PostCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostCategoryServiceImpl implements PostCategoryService {
    @Autowired
    private PostCategoryRepository postCategoryRepository;
    
    private final List<Status> N_D_S_STATUS_LIST = Status.getAllNonDefaultSearchStatus();

    /* CREATE */
    @Override
    public boolean createPostCategory(CreatePostCategoryModel createPostCategoryModel) {
        PostCategory postCategory = new PostCategory();

        postCategory.setPostCategoryName(createPostCategoryModel.getPostCategoryName());
        postCategory.setPostCategoryDesc(createPostCategoryModel.getPostCategoryDesc());
        postCategory.setStatus(Status.ACTIVE);
        postCategory.setCreatedAt(LocalDateTime.now());

        postCategoryRepository.saveAndFlush(postCategory);

        return true;
    }

    /* READ */
    @Override
    public List<ShowPostCategoryModel> getAllModel(Pageable paging) {
        Page<PostCategory> pagingResult = 
                postCategoryRepository.findAllByStatusNotIn(N_D_S_STATUS_LIST, paging);

        return getShowPostCategoryModels(pagingResult);
    }

    @Override
    public boolean existsById(Long postCategoryId) {
        return postCategoryRepository
                .existsByPostCategoryIdAndStatusNotIn(postCategoryId, N_D_S_STATUS_LIST);
    }
    @Override
    public PostCategory getById(Long postCategoryId) {
        return postCategoryRepository
                .findByPostCategoryIdAndStatusNotIn(postCategoryId, N_D_S_STATUS_LIST)
                .orElse(null);
    }

    @Override
    public boolean existsByPostCategoryName(String postCategoryName) {
        return postCategoryRepository
                .existsByPostCategoryNameAndStatusNotIn(postCategoryName, N_D_S_STATUS_LIST);
    }
    @Override
    public List<ShowPostCategoryModel> getAllModelByPostCategoryNameContains(String postCategoryName, Pageable paging) {
        Page<PostCategory> pagingResult =
                postCategoryRepository
                        .findByPostCategoryNameContainsAndStatusNotIn(postCategoryName, N_D_S_STATUS_LIST, paging);

        return getShowPostCategoryModels(pagingResult);
    }

    @Override
    public List<ShowPostCategoryModel> getAllModelByPostCategoryDescContains(String postCategoryDesc, Pageable paging) {
        Page<PostCategory> pagingResult =
                postCategoryRepository
                        .findByPostCategoryDescContainsAndStatusNotIn(postCategoryDesc, N_D_S_STATUS_LIST, paging);

        return getShowPostCategoryModels(pagingResult);
    }

    /* UPDATE */
    @Override
    public boolean updatePostCategory(UpdatePostCategoryModel updatePostCategoryModel) {
        PostCategory postCategory = getById(updatePostCategoryModel.getPostCategoryId());

        if (postCategory == null)
            return false;

        postCategory.setPostCategoryName(updatePostCategoryModel.getPostCategoryName());
        postCategory.setPostCategoryDesc(updatePostCategoryModel.getPostCategoryDesc());
        postCategory.setUpdatedAt(LocalDateTime.now());
        postCategoryRepository.saveAndFlush(postCategory);
        return true;
    }

    @Override
    public boolean deactivatePostCategory(Long postCategoryId) {
        PostCategory postCategory = getById(postCategoryId);

        if (postCategory == null)
            return false;

        postCategory.setStatus(Status.INACTIVE);
        postCategoryRepository.saveAndFlush(postCategory);

        return true;
    }

    /* DELETE */
    @Override
    public boolean deletePostCategory(Long postCategoryId) {
        PostCategory postCategory = getById(postCategoryId);

        if (postCategory == null)
            return false;

        postCategory.setStatus(Status.DELETED);
        postCategoryRepository.saveAndFlush(postCategory);

        return true;
    }

    /* Utils */
    private List<ShowPostCategoryModel> getShowPostCategoryModels(Page<PostCategory> pagingResult) {
        if(pagingResult.hasContent()){
            int totalPage = pagingResult.getTotalPages();

            Page<ShowPostCategoryModel> modelResult = pagingResult.map(new Converter<PostCategory, ShowPostCategoryModel>() {

                @Override
                protected ShowPostCategoryModel doForward(PostCategory postCategory) {
                    ShowPostCategoryModel model = new ShowPostCategoryModel();

                    model.setPostCategoryId(postCategory.getPostCategoryId());
                    model.setPostCategoryDesc(postCategory.getPostCategoryDesc());
                    model.setPostCategoryName(postCategory.getPostCategoryName());
                    model.setStatus(postCategory.getStatus());

                    model.setCreatedAt(postCategory.getCreatedAt());
                    model.setCreatedBy(postCategory.getCreatedBy());
                    model.setUpdatedAt(postCategory.getCreatedAt());
                    model.setUpdatedBy(postCategory.getUpdatedBy());
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected PostCategory doBackward(ShowPostCategoryModel showPostCategoryModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        }else{
            return new ArrayList<ShowPostCategoryModel>();
        }
    }
}
