package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.Enum.Status;
import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.entities.Post;
import com.ntv.ntvcons_backend.entities.PostCategory;
import com.ntv.ntvcons_backend.entities.PostModels.CreatePostModel;
import com.ntv.ntvcons_backend.entities.PostModels.ShowPostModel;
import com.ntv.ntvcons_backend.entities.PostModels.UpdatePostModel;
import com.ntv.ntvcons_backend.repositories.PostCategoryRepository;
import com.ntv.ntvcons_backend.repositories.PostRepository;
import com.ntv.ntvcons_backend.services.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostCategoryRepository postCategoryRepository;

    /* CREATE */
    @PreAuthorize("hasAnyAuthority('24','54')")
    @PostMapping(value = "/v1/createPost", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createPost(@RequestBody CreatePostModel createPostModel){
        if(postRepository.existsByAddressAndIsDeletedIsFalse(createPostModel.getAddress())){
            return ResponseEntity.ok().body("Địa chỉ đã tồn tại dự án khác.");
        }else{
            if(!postCategoryRepository.existsById(createPostModel.getPostCategoryId())){
                return ResponseEntity.ok().body("PostCategoryId không tồn tại.");
            }else{
                boolean result = postService.createPost(createPostModel);
                if (result) {
                    return ResponseEntity.ok().body("Tạo thành công.");
                }
                return ResponseEntity.badRequest().body("Tạo thất bại.");
            }
        }

    }

    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
                                             @RequestParam SearchType.POST searchType) {
        try {
            Post post;

            switch (searchType) {
                case BY_POST_ID:
                    post = postService.getPostById(Long.parseLong(searchParam));

                    if (post == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Post found with Id: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity Post");
            }

            return ResponseEntity.ok().body(post);
        } catch (NumberFormatException nFE) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(
                            "Invalid parameter type for searchType: '" + searchType
                                    + "'. Expecting parameter of type: Long",
                            nFE.getMessage()));
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid searchType */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            String errorMsg = "Error searching for Post with ";

            switch (searchType) {
                case BY_POST_ID:
                    errorMsg += "Id: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
                                                @RequestParam SearchType.ALL_POST searchType,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortBy,
                                                @RequestParam boolean sortType) {
        try {
            List<ShowPostModel> post;

            switch (searchType) {

                case BY_POST_CATEGORY_ID:
                    post = postService.getPostByPostCategory(Long.parseLong(searchParam), pageNo, pageSize, sortBy, sortType);

                    if (post == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Post found with PostCategoryId: '" + searchParam + "'. ");
                    }
                    break;

                case BY_SCALE:
                    post = postService.getPostByScale(searchParam, pageNo, pageSize, sortBy, sortType);

                    if (post == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Post found with Scale: '" + searchParam + "'. ");
                    }
                    break;

                case BY_AUTHOR_NAME:
                    post = postService.getPostByAuthorName(searchParam, pageNo, pageSize, sortBy, sortType);

                    if (post == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Post found with Author Name: '" + searchParam + "'. ");
                    }
                    break;

                case BY_POST_TITLE:
                    post = postService.getPostByPostTitle(searchParam, pageNo, pageSize, sortBy, sortType);

                    if (post == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Post found with Post Title: '" + searchParam + "'. ");
                    }
                    break;

                case BY_OWNER_NAME:
                    post = postService.getPostByOwnerName(searchParam, pageNo, pageSize, sortBy, sortType);

                    if (post == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Post found with Owner Name: '" + searchParam + "'. ");
                    }
                    break;

                case BY_ADDRESS:
                    post = postService.getPostByAddress(searchParam, pageNo, pageSize, sortBy, sortType);

                    if (post == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No Post found with Address: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity Post");
            }

            return ResponseEntity.ok().body(post);
        } catch (NumberFormatException nFE) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(
                            "Invalid parameter type for searchType: '" + searchType
                                    + "'. Expecting parameter of type: Long",
                            nFE.getMessage()));
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid searchType */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            String errorMsg = "Error searching for Post with ";

            switch (searchType) {

                case BY_POST_CATEGORY_ID:
                    errorMsg += "PostCategoryId: '" + searchParam + "'. ";
                    break;

                case BY_AUTHOR_NAME:
                    errorMsg += "Author Name: '" + searchParam + "'. ";
                    break;

                case BY_POST_TITLE:
                    errorMsg += "Post Title: '" + searchParam + "'. ";
                    break;

                case BY_OWNER_NAME:
                    errorMsg += "Owner Name: '" + searchParam + "'. ";
                    break;

                case BY_ADDRESS:
                    errorMsg += "Address: '" + searchParam + "'. ";
                    break;

                case BY_SCALE:
                    errorMsg += "Scale: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortTypeAsc) {
        try {
            List<ShowPostModel> posts = postService.getAllAvailablePost(pageNo, pageSize, sortBy, sortTypeAsc);

            if (posts == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Post found");
            }

            return ResponseEntity.ok().body(posts);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for Request", e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('54','24')")
    @GetMapping(value = "/v1/getCategoryForCreatePost", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getCategoryForCreatePost() {
            List<PostCategory> categoryModelList = postService.getCategoryForCreatePost();
            if (categoryModelList == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No PostCategory found");
            }
            return ResponseEntity.ok().body(categoryModelList);
    }

    /* UPDATE */
    @PreAuthorize("hasAnyAuthority('54','24')")
    @PutMapping(value = "/v1/updatePost", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updatePost(@RequestBody UpdatePostModel updatePostModel) {
        boolean result = postService.updatePost(updatePostModel);

        if(result) {
            return ResponseEntity.ok().body("Cập nhật thành công.");
        }

        return ResponseEntity.badRequest().body("Cập nhật thất bại.");
    }

    /* DELETE */
    @PreAuthorize("hasAnyAuthority('54')")
    @DeleteMapping(value = "/v1/deletePost/{postId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deletePost(@PathVariable(name = "postId") Long postId) {
        try {
            if (!postService.deletePost(postId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No Post found with Id: '" + postId + "'. ");
            }

            return ResponseEntity.ok().body("Deleted Post with Id: '" + postId + "'. ");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting Post with Id: '" + postId + "'. ", e.getMessage()));
        }
    }
}
