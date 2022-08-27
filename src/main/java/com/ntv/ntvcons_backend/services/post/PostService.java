package com.ntv.ntvcons_backend.services.post;

import com.ntv.ntvcons_backend.entities.Post;
import com.ntv.ntvcons_backend.entities.PostCategory;
import com.ntv.ntvcons_backend.entities.PostModels.CreatePostModel;
import com.ntv.ntvcons_backend.entities.PostModels.ShowPostModel;
import com.ntv.ntvcons_backend.entities.PostModels.UpdatePostModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    /* CREATE */
    Post createPost(CreatePostModel createPostModel);

    /* READ */;
    List<ShowPostModel> getAllAvailablePost(Pageable paging) throws Exception;

    boolean existsById(long postId) throws Exception;
    Post getById(Long postId);

    List<ShowPostModel> getAllModelByPostCategoryId(Long postCategoryId, Pageable paging) throws Exception;

    List<ShowPostModel> getAllModelByAuthorNameContains(String authorName, Pageable paging) throws Exception;

    List<ShowPostModel> getAllModelByPostTitleContains(String postTitle, Pageable paging) throws Exception;

    List<ShowPostModel> getAllModelByOwnerNameContains(String ownerName, Pageable paging) throws Exception;

    boolean existsByAddress(String address);
    List<ShowPostModel> getAllModelByAddressContains(String address, Pageable paging) throws Exception;

    List<ShowPostModel> getAllModelByScaleContains(String scale, Pageable paging) throws Exception;

    /* Ko nên để đây, cần thì gọi api của postCategory */
    List<PostCategory> getAllPostCategoryForCreatePost();

    /* UPDATE */
    Post updatePost(UpdatePostModel updatePostModel);

    /* DELETE */
    boolean deletePost(Long postId);
}
