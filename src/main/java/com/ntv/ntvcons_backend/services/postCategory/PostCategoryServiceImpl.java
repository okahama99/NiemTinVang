package com.ntv.ntvcons_backend.services.postCategory;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.Enum.Status;
import com.ntv.ntvcons_backend.entities.Post;
import com.ntv.ntvcons_backend.entities.PostCategory;
import com.ntv.ntvcons_backend.entities.PostCategoryModels.CreatePostCategoryModel;
import com.ntv.ntvcons_backend.entities.PostCategoryModels.ShowPostCategoryModel;
import com.ntv.ntvcons_backend.entities.PostCategoryModels.UpdatePostCategoryModel;
import com.ntv.ntvcons_backend.repositories.PostCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostCategoryServiceImpl implements PostCategoryService {

    @Autowired
    PostCategoryRepository postCategoryRepository;

    @Override
    public boolean createPostCategory(CreatePostCategoryModel createPostCategoryModel) {
        PostCategory postCategory = new PostCategory();
        postCategory.setPostCategoryDesc(createPostCategoryModel.getPostCategoryDesc());
        postCategory.setPostCategoryName(createPostCategoryModel.getPostCategoryName());
        postCategory.setStatus(Status.ACTIVE);
        postCategory.setCreatedAt(LocalDateTime.now());
        postCategoryRepository.saveAndFlush(postCategory);
        return true;
    }

    @Override
    public List<ShowPostCategoryModel> getAllAvailablePostCategory(int pageNo, int pageSize, String sortBy, boolean sortType) {
        Pageable paging;
        if(sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        }else{
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<PostCategory> pagingResult = postCategoryRepository.findAllByStatus(paging, Status.ACTIVE);

        if(pagingResult.hasContent()){
            double totalPage = Math.ceil((double)pagingResult.getTotalElements() / pageSize);

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

    @Override
    public List<ShowPostCategoryModel> getByPostCategoryName(String postCategoryName, int pageNo, int pageSize, String sortBy, boolean sortType) {
        Pageable paging;
        if(sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        }else{
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<PostCategory> pagingResult = postCategoryRepository.findByPostCategoryNameAndStatus(postCategoryName, paging, Status.ACTIVE);

        if(pagingResult.hasContent()){
            double totalPage = Math.ceil((double)pagingResult.getTotalElements() / pageSize);

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

    @Override
    public List<ShowPostCategoryModel> getByPostCategoryDesc(String postCategoryDesc, int pageNo, int pageSize, String sortBy, boolean sortType) {
        Pageable paging;
        if(sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        }else{
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<PostCategory> pagingResult = postCategoryRepository.findByPostCategoryDescAndStatus(postCategoryDesc, paging, Status.ACTIVE);

        if(pagingResult.hasContent()){
            double totalPage = Math.ceil((double)pagingResult.getTotalElements() / pageSize);

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

    @Override
    public PostCategory getPostCategoryById(Long postCategoryId) {
        Optional<PostCategory> postCategory = postCategoryRepository.findByPostCategoryIdAndStatus(postCategoryId, Status.ACTIVE);
        if(postCategory.isPresent())
        {
            return postCategory.get();
        }
        return null;
    }

    @Override
    public boolean updatePostCategory(UpdatePostCategoryModel updatePostCategoryModel) {
        PostCategory postCategory = postCategoryRepository.findById(updatePostCategoryModel.getPostCategoryId()).orElse(null);
        if(postCategory != null)
        {
            postCategory.setPostCategoryName(updatePostCategoryModel.getPostCategoryName());
            postCategory.setPostCategoryDesc(updatePostCategoryModel.getPostCategoryDesc());
            postCategory.setUpdatedAt(LocalDateTime.now());
            postCategoryRepository.saveAndFlush(postCategory);
            return true;
        }
        return false;
    }

    @Override
    public boolean deletePostCategory(Long postCategoryId) {
        PostCategory postCategory = postCategoryRepository.findById(postCategoryId).orElse(null);

        if (postCategory == null){
            return false;
        }

        postCategory.setStatus(Status.INACTIVE);
        postCategoryRepository.saveAndFlush(postCategory);

        return true;
    }
}
