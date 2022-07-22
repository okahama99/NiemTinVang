package com.ntv.ntvcons_backend.services.postCategory;

import com.ntv.ntvcons_backend.entities.PostCategory;
import com.ntv.ntvcons_backend.entities.PostCategoryModels.CreatePostCategoryModel;
import com.ntv.ntvcons_backend.entities.PostCategoryModels.ShowPostCategoryModel;
import com.ntv.ntvcons_backend.entities.PostCategoryModels.UpdatePostCategoryModel;

import java.util.List;

public interface PostCategoryService {
    /* CREATE */
    boolean createPostCategory(CreatePostCategoryModel createPostCategoryModel);

    /* READ */;
    List<ShowPostCategoryModel> getAllAvailablePostCategory(int pageNo, int pageSize, String sortBy, boolean sortType);

    List<ShowPostCategoryModel> getByPostCategoryName(String postCategoryName, int pageNo, int pageSize, String sortBy, boolean sortType);

    List<ShowPostCategoryModel> getByPostCategoryDesc(String postCategoryDesc, int pageNo, int pageSize, String sortBy, boolean sortType);

    PostCategory getPostCategoryById(Long postCategoryId);

    /* UPDATE */
    boolean updatePostCategory(UpdatePostCategoryModel updatePostCategoryModel);

    /* DELETE */
    boolean deletePostCategory(Long postCategoryId);
}
