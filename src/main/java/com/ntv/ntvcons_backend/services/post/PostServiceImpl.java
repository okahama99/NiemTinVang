package com.ntv.ntvcons_backend.services.post;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.Enum.Status;
import com.ntv.ntvcons_backend.entities.*;
import com.ntv.ntvcons_backend.entities.PostCategoryModels.ShowPostCategoryModel;
import com.ntv.ntvcons_backend.entities.PostModels.CreatePostModel;
import com.ntv.ntvcons_backend.entities.PostModels.ShowPostModel;
import com.ntv.ntvcons_backend.entities.PostModels.UpdatePostModel;
import com.ntv.ntvcons_backend.repositories.PostCategoryRepository;
import com.ntv.ntvcons_backend.repositories.PostRepository;
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
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostCategoryRepository postCategoryRepository;

    @Override
    public boolean createPost(CreatePostModel createPostModel) {
        Post post = new Post();
        post.setAddress(createPostModel.getAddress());
        post.setPostCategoryId(createPostModel.getPostCategoryId());
        post.setPostTitle(createPostModel.getPostTitle());
        post.setAuthorName(createPostModel.getAuthorName());
        post.setScale(createPostModel.getScale());
        post.setOwnerName(createPostModel.getOwnerName());
        post.setStatus(Status.ACTIVE);
        post.setCreatedAt(LocalDateTime.now());
        postRepository.saveAndFlush(post);
        return true;
    }

    @Override
    public List<ShowPostModel> getAllAvailablePost(int pageNo, int pageSize, String sortBy, boolean sortType) {
        Pageable paging;
        if(sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        }else{
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<Post> pagingResult = postRepository.findAllByIsDeletedIsFalse(paging);

        if(pagingResult.hasContent()){
            double totalPage = Math.ceil((double)pagingResult.getTotalElements() / pageSize);

            Page<ShowPostModel> modelResult = pagingResult.map(new Converter<Post, ShowPostModel>() {

                @Override
                protected ShowPostModel doForward(Post post) {
                    ShowPostModel model = new ShowPostModel();

                    Optional<PostCategory> postCategory = postCategoryRepository.findByPostCategoryIdAndStatus(post.getPostCategoryId(), Status.ACTIVE);
                    if(postCategory.isPresent())
                    {
                        model.setPostCategoryDesc(postCategory.get().getPostCategoryDesc());
                        model.setPostCategoryName(postCategory.get().getPostCategoryName());
                    }else{
                        model.setPostCategoryDesc(null);
                        model.setPostCategoryName(null);
                    }

                    model.setPostId(post.getPostId());
                    model.setPostCategoryId(post.getPostCategoryId());
                    model.setAuthorName(post.getAuthorName());
                    model.setPostTitle(post.getPostTitle());
                    model.setOwnerName(post.getOwnerName());
                    model.setAddress(post.getAddress());
                    model.setScale(post.getScale());
                    model.setStatus(post.getStatus());

                    model.setCreatedAt(post.getCreatedAt());
                    model.setCreatedBy(post.getCreatedBy());
                    model.setUpdatedAt(post.getCreatedAt());
                    model.setUpdatedBy(post.getUpdatedBy());
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected Post doBackward(ShowPostModel showPostModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        }else{
            return new ArrayList<ShowPostModel>();
        }
    }

    @Override
    public List<ShowPostCategoryModel> getCategoryForCreatePost() {
        List<ShowPostCategoryModel> postCategoryModelList = postCategoryRepository.findAllByStatus(Status.ACTIVE);
        return postCategoryModelList;
    }

    @Override
    public boolean updatePost(UpdatePostModel updatePostModel) {
        Post post = postRepository.findById(updatePostModel.getPostId()).orElse(null);
        Optional<PostCategory> category = postCategoryRepository.findById(updatePostModel.getPostCategoryId());
        if(post != null || category != null)
        {
            post.setPostCategoryId(updatePostModel.getPostCategoryId());
            post.setAuthorName(updatePostModel.getAuthorName());
            post.setPostTitle(updatePostModel.getPostTitle());
            post.setOwnerName(updatePostModel.getOwnerName());
            post.setAddress(updatePostModel.getAddress());
            post.setScale(updatePostModel.getScale());
            post.setUpdatedAt(LocalDateTime.now());
            postRepository.saveAndFlush(post);
            return true;
        }
        return false;
    }

    @Override
    public boolean deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);

        if (post == null){
            return false;
        }

        post.setIsDeleted(true);
        postRepository.saveAndFlush(post);

        return true;
    }

    @Override
    public List<ShowPostModel> getPostByPostCategory(Long postCategoryId, int pageNo, int pageSize, String sortBy, boolean sortType) {
        Pageable paging;
        if(sortType) {
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        }else{
            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }

        Page<Post> pagingResult = postRepository.findByPostCategoryIdAndIsDeletedIsFalse(postCategoryId, paging);

        if(pagingResult.hasContent()){
            double totalPage = Math.ceil((double)pagingResult.getTotalElements() / pageSize);

            Page<ShowPostModel> modelResult = pagingResult.map(new Converter<Post, ShowPostModel>() {

                @Override
                protected ShowPostModel doForward(Post post) {
                    ShowPostModel model = new ShowPostModel();

                    Optional<PostCategory> postCategory = postCategoryRepository.findByPostCategoryIdAndStatus(post.getPostCategoryId(), Status.ACTIVE);
                    if(postCategory.isPresent())
                    {
                        model.setPostCategoryDesc(postCategory.get().getPostCategoryDesc());
                        model.setPostCategoryName(postCategory.get().getPostCategoryName());
                    }else{
                        model.setPostCategoryDesc(null);
                        model.setPostCategoryName(null);
                    }

                    model.setPostId(post.getPostId());
                    model.setPostCategoryId(post.getPostCategoryId());
                    model.setAuthorName(post.getAuthorName());
                    model.setPostTitle(post.getPostTitle());
                    model.setOwnerName(post.getOwnerName());
                    model.setAddress(post.getAddress());
                    model.setScale(post.getScale());
                    model.setStatus(post.getStatus());

                    model.setCreatedAt(post.getCreatedAt());
                    model.setCreatedBy(post.getCreatedBy());
                    model.setUpdatedAt(post.getCreatedAt());
                    model.setUpdatedBy(post.getUpdatedBy());
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected Post doBackward(ShowPostModel showPostModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        }else{
            return new ArrayList<ShowPostModel>();
        }
    }
    /* CREATE */

    /* READ */;

    /* UPDATE */

    /* DELETE */

}
