package com.ntv.ntvcons_backend.services.post;

import com.ntv.ntvcons_backend.entities.Post;
import com.ntv.ntvcons_backend.entities.PostCategory;
import com.ntv.ntvcons_backend.entities.PostModels.CreatePostModel;
import com.ntv.ntvcons_backend.entities.PostModels.ShowPostModel;
import com.ntv.ntvcons_backend.entities.PostModels.UpdatePostModel;

import java.util.List;

public interface PostService {
    /* CREATE */
    boolean createPost(CreatePostModel createPostModel);

    /* READ */;
    List<ShowPostModel> getAllAvailablePost(int pageNo, int pageSize, String sortBy, boolean sortType);

    List<ShowPostModel> getPostByPostCategory(Long postCategoryId, int pageNo, int pageSize, String sortBy, boolean sortType);

    List<ShowPostModel> getPostByScale(String scale, int pageNo, int pageSize, String sortBy, boolean sortType);

    List<ShowPostModel> getPostByAuthorName(String authorName, int pageNo, int pageSize, String sortBy, boolean sortType);

    List<ShowPostModel> getPostByPostTitle(String postTitle, int pageNo, int pageSize, String sortBy, boolean sortType);

    List<ShowPostModel> getPostByOwnerName(String ownerName, int pageNo, int pageSize, String sortBy, boolean sortType);

    List<ShowPostModel> getPostByAddress(String address, int pageNo, int pageSize, String sortBy, boolean sortType);

    List<PostCategory> getCategoryForCreatePost();

    Post getPostById(Long postId);

    /* UPDATE */
    boolean updatePost(UpdatePostModel updatePostModel);

    /* DELETE */
    boolean deletePost(Long postId);


}
