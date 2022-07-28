package com.ntv.ntvcons_backend.services.post;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.entities.Post;
import com.ntv.ntvcons_backend.entities.PostCategory;
import com.ntv.ntvcons_backend.entities.PostModels.CreatePostModel;
import com.ntv.ntvcons_backend.entities.PostModels.ShowPostModel;
import com.ntv.ntvcons_backend.entities.PostModels.UpdatePostModel;
import com.ntv.ntvcons_backend.repositories.PostCategoryRepository;
import com.ntv.ntvcons_backend.repositories.PostRepository;
import com.ntv.ntvcons_backend.services.postCategory.PostCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostCategoryRepository postCategoryRepository;
    @Autowired
    private PostCategoryService postCategoryService;

    private final List<Status> N_D_S_STATUS_LIST = Status.getAllNonDefaultSearchStatus();

    /* CREATE */
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

    /* READ */
    @Override
    public List<ShowPostModel> getAllAvailablePost(Pageable paging) {
        Page<Post> pagingResult =
                postRepository.findAllByStatusNotIn(N_D_S_STATUS_LIST, paging);

        return fillAllShowPostModel(pagingResult);
    }

    @Override
    public Post getById(Long postId) {
        return postRepository
                .findByPostIdAndStatusNotIn(postId, N_D_S_STATUS_LIST)
                .orElse(null);
    }

    @Override
    public List<ShowPostModel> getAllModelByPostCategoryId(Long postCategoryId, Pageable paging) {
        Page<Post> pagingResult = 
                postRepository.findAllByPostCategoryIdAndStatusNotIn(postCategoryId, N_D_S_STATUS_LIST, paging);

        return fillAllShowPostModel(pagingResult);
    }

    @Override
    public List<ShowPostModel> getAllModelByAuthorNameContains(String authorName, Pageable paging) {
        Page<Post> pagingResult =
                postRepository.findAllByAuthorNameContainsAndStatusNotIn(authorName, N_D_S_STATUS_LIST, paging);

        return fillAllShowPostModel(pagingResult);
    }

    @Override
    public List<ShowPostModel> getAllModelByPostTitleContains(String postTitle, Pageable paging) {
        Page<Post> pagingResult =
                postRepository.findAllByPostTitleContainsAndStatusNotIn(postTitle, N_D_S_STATUS_LIST, paging);

        return fillAllShowPostModel(pagingResult);
    }

    @Override
    public List<ShowPostModel> getAllModelByOwnerNameContains(String ownerName, Pageable paging) {
        Page<Post> pagingResult =
                postRepository.findAllByOwnerNameContainsAndStatusNotIn(ownerName, N_D_S_STATUS_LIST, paging);

        return fillAllShowPostModel(pagingResult);
    }

    @Override
    public boolean existsByAddress(String address) {
        return postRepository
                .existsByAddressAndStatusNotIn(address, N_D_S_STATUS_LIST);
    }
    @Override
    public List<ShowPostModel> getAllModelByAddressContains(String address, Pageable paging) {
        Page<Post> pagingResult =
                postRepository.findAllByAddressContainsAndStatusNotIn(address, N_D_S_STATUS_LIST, paging);

        return fillAllShowPostModel(pagingResult);
    }

    @Override
    public List<ShowPostModel> getAllModelByScaleContains(String scale, Pageable paging) {
        Page<Post> pagingResult =
                postRepository.findAllByScaleContainsAndStatusNotIn(scale, N_D_S_STATUS_LIST, paging);

        return fillAllShowPostModel(pagingResult);
    }

    @Override
    public List<PostCategory> getAllPostCategoryForCreatePost() {
        List<PostCategory> postCategoryModelList = postCategoryRepository.findAllByStatusNotIn(N_D_S_STATUS_LIST);
        return postCategoryModelList;
    }

    /* UPDATE */
    @Override
    public boolean updatePost(UpdatePostModel updatePostModel) {
        Post post = getById(updatePostModel.getPostId());

        if (post == null)
            return false;

        PostCategory category =
                postCategoryService.getById(updatePostModel.getPostCategoryId());

        if (category == null)
            return false;

        post.setPostCategoryId(category.getPostCategoryId());
        post.setAuthorName(updatePostModel.getAuthorName());
        post.setPostTitle(updatePostModel.getPostTitle());
        post.setOwnerName(updatePostModel.getOwnerName());
        post.setAddress(updatePostModel.getAddress());
        post.setScale(updatePostModel.getScale());
        post.setUpdatedAt(LocalDateTime.now());

        postRepository.saveAndFlush(post);

        return true;
    }

    /* DELETE */
    @Override
    public boolean deletePost(Long postId) {
        Post post = getById(postId);

        if (post == null)
            return false;

        post.setStatus(Status.DELETED);
        postRepository.saveAndFlush(post);

        return true;
    }

    /* Utils */
    private List<ShowPostModel> fillAllShowPostModel(Page<Post> pagingResult) {
        if (pagingResult.hasContent()) {
            int totalPage = pagingResult.getTotalPages();

            Page<ShowPostModel> modelResult = pagingResult.map(new Converter<Post, ShowPostModel>() {

                @Override
                protected ShowPostModel doForward(Post post) {
                    ShowPostModel model = new ShowPostModel();

                    Optional<PostCategory> postCategory = postCategoryRepository.findByPostCategoryIdAndStatusNotIn(post.getPostCategoryId(), N_D_S_STATUS_LIST);
                    if (postCategory.isPresent()) {
                        model.setPostCategoryDesc(postCategory.get().getPostCategoryDesc());
                        model.setPostCategoryName(postCategory.get().getPostCategoryName());
                    } else {
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
        } else {
            return new ArrayList<ShowPostModel>();
        }
    }
}
