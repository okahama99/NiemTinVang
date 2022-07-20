package com.ntv.ntvcons_backend.services.post;

import com.ntv.ntvcons_backend.entities.PostCategoryModels.ShowPostCategoryModel;
import com.ntv.ntvcons_backend.entities.PostModels.CreatePostModel;
import com.ntv.ntvcons_backend.entities.PostModels.ShowPostModel;
import com.ntv.ntvcons_backend.entities.PostModels.UpdatePostModel;

import java.util.List;

public interface PostService {
    /* CREATE */
    boolean createPost(CreatePostModel createPostModel);

    /* READ */;
    List<ShowPostModel> getAllAvailablePost(int pageNo, int pageSize, String sortBy, boolean sortType);

    List<ShowPostCategoryModel> getCategoryForCreatePost();

    /* UPDATE */
    boolean updatePost(UpdatePostModel updatePostModel);

    /* DELETE */
    boolean deletePost(Long postId);

    List<ShowPostModel> getPostByPostCategory(Long postCategoryId, int pageNo, int pageSize, String sortBy, boolean sortType);
}
