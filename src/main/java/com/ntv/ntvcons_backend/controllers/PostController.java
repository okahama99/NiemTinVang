package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.entities.PostCategoryModels.ShowPostCategoryModel;
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
    //@PreAuthorize("hasAnyRole('Staff','Admin')")
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

    /* READ */
    //@PreAuthorize("hasAnyRole('Admin','Staff')")
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

    //@PreAuthorize("hasAnyRole('Admin','Staff')")
    @GetMapping(value = "/v1/getCategoryForCreatePost", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getCategoryForCreatePost() {
            List<ShowPostCategoryModel> requests = postService.getCategoryForCreatePost();
            if (requests == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No PostCategory found");
            }
            return ResponseEntity.ok().body(requests);
    }

    //@PreAuthorize("hasAnyRole('Engineer','Admin','Staff','Customer')")
    @GetMapping(value = "/v1/getPostByPostCategory", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getPostByPostCategory(@RequestParam Long postCategoryId,
                                                 @RequestParam int pageNo,
                                                 @RequestParam int pageSize,
                                                 @RequestParam String sortBy,
                                                 @RequestParam boolean sortTypeAsc) {
        try {
            List<ShowPostModel> requests = postService.getPostByPostCategory(postCategoryId, pageNo, pageSize, sortBy, sortTypeAsc);

            if (requests == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Post found");
            }

            return ResponseEntity.ok().body(requests);
        } catch (PropertyReferenceException | IllegalArgumentException pROrIAE) {
            /* Catch invalid sortBy */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", pROrIAE.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error searching for Request", e.getMessage()));
        }
    }

    /* UPDATE */
    //@PreAuthorize("hasAnyRole('Admin','Staff')")
    @PutMapping(value = "/v1/updatePost", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updatePost(@RequestBody UpdatePostModel updatePostModel) {
        boolean result = postService.updatePost(updatePostModel);

        if(result) {
            return ResponseEntity.ok().body("Cập nhật thành công.");
        }

        return ResponseEntity.badRequest().body("Cập nhật thất bại.");
    }

    /* DELETE */
    //@PreAuthorize("hasAnyRole('Admin')")
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
