package com.ntv.ntvcons_backend.services.postCategory;

import com.ntv.ntvcons_backend.entities.PostCategory;
import com.ntv.ntvcons_backend.entities.PostCategoryModels.CreatePostCategoryModel;
import com.ntv.ntvcons_backend.entities.PostCategoryModels.ShowPostCategoryModel;
import com.ntv.ntvcons_backend.entities.PostCategoryModels.UpdatePostCategoryModel;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostCategoryService {
    /* CREATE */
    boolean createPostCategory(CreatePostCategoryModel createPostCategoryModel);

    /* READ */;
    List<ShowPostCategoryModel> getAllModel(Pageable paging);

    boolean existsById(Long postCategoryId);
    PostCategory getById(Long postCategoryId);

    boolean existsByPostCategoryName(String postCategoryName);
    List<ShowPostCategoryModel> getAllModelByPostCategoryNameContains(String postCategoryName, Pageable paging);

    List<ShowPostCategoryModel> getAllModelByPostCategoryDescContains(String postCategoryDesc, Pageable paging);

    /* UPDATE */
    boolean updatePostCategory(UpdatePostCategoryModel updatePostCategoryModel);

    boolean deactivatePostCategory(Long postCategoryId);

    /* DELETE */
    boolean deletePostCategory(Long postCategoryId);
}
