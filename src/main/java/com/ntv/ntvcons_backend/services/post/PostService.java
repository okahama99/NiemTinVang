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
    boolean createPost(CreatePostModel createPostModel);

    /* READ */;
    List<ShowPostModel> getAllAvailablePost(Pageable paging);

    Post getById(Long postId);

    List<ShowPostModel> getAllModelByPostCategoryId(Long postCategoryId, Pageable paging);

    List<ShowPostModel> getAllModelByAuthorNameContains(String authorName, Pageable paging);

    List<ShowPostModel> getAllModelByPostTitleContains(String postTitle, Pageable paging);

    List<ShowPostModel> getAllModelByOwnerNameContains(String ownerName, Pageable paging);

    boolean existsByAddress(String address);
    List<ShowPostModel> getAllModelByAddressContains(String address, Pageable paging);

    List<ShowPostModel> getAllModelByScaleContains(String scale, Pageable paging);

    /* Ko nên để đây, cần thì gọi api của postCategory */
    List<PostCategory> getAllPostCategoryForCreatePost();

    /* UPDATE */
    boolean updatePost(UpdatePostModel updatePostModel);

    /* DELETE */
    boolean deletePost(Long postId);
}
