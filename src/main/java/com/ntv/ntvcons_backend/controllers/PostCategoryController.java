package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.Enum.Status;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.entities.PostCategoryModels.CreatePostCategoryModel;
import com.ntv.ntvcons_backend.entities.PostCategoryModels.ShowPostCategoryModel;
import com.ntv.ntvcons_backend.entities.PostCategoryModels.UpdatePostCategoryModel;
import com.ntv.ntvcons_backend.repositories.PostCategoryRepository;
import com.ntv.ntvcons_backend.services.postCategory.PostCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/postCategory")
public class PostCategoryController {

    @Autowired
    PostCategoryRepository postCategoryRepository;

    @Autowired
    PostCategoryService postCategoryService;

    /* CREATE */
    //@PreAuthorize("hasAnyRole('Admin')")
    @PostMapping(value = "/v1/createPostCategory", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createPostCategory(@RequestBody CreatePostCategoryModel createPostCategoryModel){
        if((postCategoryRepository.findByPostCategoryNameAndStatus(createPostCategoryModel.getPostCategoryName(), Status.ACTIVE)) != null){
            return ResponseEntity.ok().body("Tên đã tồn tại, vui lòng sử dụng tên khác.");
        }else{
                boolean result = postCategoryService.createPostCategory(createPostCategoryModel);
                if (result) {
                    return ResponseEntity.ok().body("Tạo thành công.");
                }
                return ResponseEntity.badRequest().body("Tạo thất bại.");
        }

    }

    /* READ */
    @GetMapping(value = "/v1/getAll", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAll(@RequestParam int pageNo,
                                         @RequestParam int pageSize,
                                         @RequestParam String sortBy,
                                         @RequestParam boolean sortTypeAsc) {
        try {
            List<ShowPostCategoryModel> posts = postCategoryService.getAllAvailablePostCategory(pageNo, pageSize, sortBy, sortTypeAsc);

            if (posts == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No PostCategory found");
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

    /* UPDATE */
    //@PreAuthorize("hasAnyRole('Admin','Staff')")
    @PutMapping(value = "/v1/updatePostCategory", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> updatePostCategory(@RequestBody UpdatePostCategoryModel updatePostCategoryModel) {
        boolean result = postCategoryService.updatePostCategory(updatePostCategoryModel);

        if(result) {
            return ResponseEntity.ok().body("Cập nhật thành công.");
        }

        return ResponseEntity.badRequest().body("Cập nhật thất bại.");
    }

    /* DELETE */
    //@PreAuthorize("hasAnyRole('Admin')")
    @DeleteMapping(value = "/v1/deletePostCategory/{postCategoryId}", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> deletePostCategory(@PathVariable(name = "postCategoryId") Long postCategoryId) {
        try {
            if (!postCategoryService.deletePostCategory(postCategoryId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No PostCategory found with Id: '" + postCategoryId + "'. ");
            }

            return ResponseEntity.ok().body("Deleted PostCategory with Id: '" + postCategoryId + "'. ");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ErrorResponse("Error deleting PostCategory with Id: '" + postCategoryId + "'. ", e.getMessage()));
        }
    }
}
