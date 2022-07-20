package com.ntv.ntvcons_backend.services.postCategory;

import com.ntv.ntvcons_backend.entities.PostCategoryModels.CreatePostCategoryModel;
import com.ntv.ntvcons_backend.entities.PostCategoryModels.ShowPostCategoryModel;
import com.ntv.ntvcons_backend.entities.PostCategoryModels.UpdatePostCategoryModel;

import java.util.List;

public interface PostCategoryService {
    /* CREATE */
    boolean createPostCategory(CreatePostCategoryModel createPostCategoryModel);

    /* READ */;
    List<ShowPostCategoryModel> getAllAvailablePostCategory(int pageNo, int pageSize, String sortBy, boolean sortType);

    /* UPDATE */
    boolean updatePostCategory(UpdatePostCategoryModel updatePostCategoryModel);

    /* DELETE */
    boolean deletePostCategory(Long postCategoryId);
}
