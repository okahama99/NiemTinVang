package com.ntv.ntvcons_backend.services.post;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileReadDTO;
import com.ntv.ntvcons_backend.entities.Post;
import com.ntv.ntvcons_backend.entities.PostCategory;
import com.ntv.ntvcons_backend.entities.PostModels.CreatePostModel;
import com.ntv.ntvcons_backend.entities.PostModels.ShowPostModel;
import com.ntv.ntvcons_backend.entities.PostModels.UpdatePostModel;
import com.ntv.ntvcons_backend.repositories.PostCategoryRepository;
import com.ntv.ntvcons_backend.repositories.PostRepository;
import com.ntv.ntvcons_backend.services.externalFileEntityWrapperPairing.ExternalFileEntityWrapperPairingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostCategoryRepository postCategoryRepository;
    @Autowired
    private ExternalFileEntityWrapperPairingService eFEWPairingService;

    private final List<Status> N_D_S_STATUS_LIST = Status.getAllNonDefaultSearchStatus();

    /* CREATE */
    @Override
    public Post createPost(CreatePostModel createPostModel) {
        Post post = new Post();

        post.setPostCategoryId(createPostModel.getPostCategoryId());

        post.setAuthorName(createPostModel.getAuthorName());
        post.setPostTitle(createPostModel.getPostTitle());
        post.setOwnerName(createPostModel.getOwnerName());
        post.setAddress(createPostModel.getAddress());
        post.setScale(createPostModel.getScale());
        post.setEstimatedCost(createPostModel.getEstimatedCost());

        post.setStatus(Status.ACTIVE);
        post.setCreatedBy(createPostModel.getCreatedBy());
        post.setCreatedAt(LocalDateTime.now());

        return postRepository.saveAndFlush(post);
    }

    /* READ */
    @Override
    public List<ShowPostModel> getAllAvailablePost(Pageable paging) throws Exception {
        Page<Post> pagingResult =
                postRepository.findAllByStatusNotIn(N_D_S_STATUS_LIST, paging);

        return fillAllShowPostModel(pagingResult);
    }

    @Override
    public boolean existsById(long postId) {
        return postRepository
                .existsByPostIdAndStatusNotIn(postId, N_D_S_STATUS_LIST);
    }

    @Override
    public Post getById(Long postId) {
        return postRepository
                .findByPostIdAndStatusNotIn(postId, N_D_S_STATUS_LIST)
                .orElse(null);
    }

    @Override
    public List<ShowPostModel> getAllModelByPostCategoryId(Long postCategoryId, Pageable paging) throws Exception {
        Page<Post> pagingResult = 
                postRepository.findAllByPostCategoryIdAndStatusNotIn(postCategoryId, N_D_S_STATUS_LIST, paging);

        return fillAllShowPostModel(pagingResult);
    }

    @Override
    public List<ShowPostModel> getAllModelByAuthorNameContains(String authorName, Pageable paging) throws Exception {
        Page<Post> pagingResult =
                postRepository.findAllByAuthorNameContainsAndStatusNotIn(authorName, N_D_S_STATUS_LIST, paging);

        return fillAllShowPostModel(pagingResult);
    }

    @Override
    public List<ShowPostModel> getAllModelByPostTitleContains(String postTitle, Pageable paging) throws Exception {
        Page<Post> pagingResult =
                postRepository.findAllByPostTitleContainsAndStatusNotIn(postTitle, N_D_S_STATUS_LIST, paging);

        return fillAllShowPostModel(pagingResult);
    }

    @Override
    public List<ShowPostModel> getAllModelByOwnerNameContains(String ownerName, Pageable paging) throws Exception {
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
    public List<ShowPostModel> getAllModelByAddressContains(String address, Pageable paging) throws Exception {
        Page<Post> pagingResult =
                postRepository.findAllByAddressContainsAndStatusNotIn(address, N_D_S_STATUS_LIST, paging);

        return fillAllShowPostModel(pagingResult);
    }

    @Override
    public List<ShowPostModel> getAllModelByScaleContains(String scale, Pageable paging) throws Exception {
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
    public Post updatePost(UpdatePostModel updatePostModel) {
        Post post = getById(updatePostModel.getPostId());

        if (post == null)
            return null;

        post.setPostCategoryId(updatePostModel.getPostCategoryId());

        post.setAuthorName(updatePostModel.getAuthorName());
        post.setPostTitle(updatePostModel.getPostTitle());
        post.setOwnerName(updatePostModel.getOwnerName());
        post.setAddress(updatePostModel.getAddress());
        post.setScale(updatePostModel.getScale());
        post.setEstimatedCost(updatePostModel.getEstimatedCost());

        post.setUpdatedAt(LocalDateTime.now());
        post.setUpdatedBy(updatePostModel.getUpdatedBy());

        return postRepository.saveAndFlush(post);
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
    private List<ShowPostModel> fillAllShowPostModel(Page<Post> pagingResult) throws Exception {
        if (pagingResult.hasContent()) {
            int totalPage = pagingResult.getTotalPages();

            Set<Long> postIdSet = pagingResult.getContent().stream()
                    .map(Post::getPostId)
                    .collect(Collectors.toSet());

            Map<Long, List<ExternalFileReadDTO>> postIdFileDTOMap =
                    eFEWPairingService.mapEntityIdExternalFileDTOListByEntityIdInAndEntityType(
                            postIdSet, EntityType.POST_ENTITY);

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
                    long postId = post.getPostId();

                    model.setPostId(postId);
                    model.setPostCategoryId(post.getPostCategoryId());
                    
                    model.setAuthorName(post.getAuthorName());
                    model.setPostTitle(post.getPostTitle());
                    model.setOwnerName(post.getOwnerName());
                    model.setAddress(post.getAddress());
                    model.setScale(post.getScale());
                    model.setEstimatedCost(post.getEstimatedCost());

                    model.setStatus(post.getStatus());
                    model.setCreatedAt(post.getCreatedAt());
                    model.setCreatedBy(post.getCreatedBy());
                    model.setUpdatedAt(post.getCreatedAt());
                    model.setUpdatedBy(post.getUpdatedBy());

                    model.setTotalPage(totalPage);

                    model.setFileList(postIdFileDTOMap.get(postId));

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
