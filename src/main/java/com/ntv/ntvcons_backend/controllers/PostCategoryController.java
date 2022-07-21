package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.Enum.Status;
import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.entities.Post;
import com.ntv.ntvcons_backend.entities.PostCategory;
import com.ntv.ntvcons_backend.entities.PostCategoryModels.CreatePostCategoryModel;
import com.ntv.ntvcons_backend.entities.PostCategoryModels.ShowPostCategoryModel;
import com.ntv.ntvcons_backend.entities.PostCategoryModels.UpdatePostCategoryModel;
import com.ntv.ntvcons_backend.entities.PostModels.ShowPostModel;
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

    @GetMapping(value = "/v1/getByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByParam(@RequestParam String searchParam,
                                             @RequestParam SearchType.POST_CATEGORY searchType) {
        try {
            PostCategory postCategory;

            switch (searchType) {
                case BY_POST_CATEGORY_ID:
                    postCategory = postCategoryService.getPostCategoryById(Long.parseLong(searchParam));

                    if (postCategory == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No PostCategory found with Id: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity PostCategory");
            }

            return ResponseEntity.ok().body(postCategory);
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
            String errorMsg = "Error searching for PostCategory with ";

            switch (searchType) {
                case BY_POST_CATEGORY_ID:
                    errorMsg += "Id: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    @GetMapping(value = "/v1/getAllByParam", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getAllByParam(@RequestParam String searchParam,
                                                @RequestParam SearchType.ALL_POST_CATEGORY searchType,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize,
                                                @RequestParam String sortBy,
                                                @RequestParam boolean sortType) {
        try {
            List<ShowPostCategoryModel> postCategory;

            switch (searchType) {

                case BY_POST_CATEGORY_NAME:
                    postCategory = postCategoryService.getByPostCategoryName(searchParam, pageNo, pageSize, sortBy, sortType);

                    if (postCategory == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No PostCategory found with PostCategoryName: '" + searchParam + "'. ");
                    }
                    break;

                case BY_POST_CATEGORY_DESC:
                    postCategory = postCategoryService.getByPostCategoryDesc(searchParam, pageNo, pageSize, sortBy, sortType);

                    if (postCategory == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("No PostCategory found with PostCategoryDesc: '" + searchParam + "'. ");
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Invalid SearchType used for entity PostCategory");
            }

            return ResponseEntity.ok().body(postCategory);
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid searchType */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            String errorMsg = "Error searching for PostCategory with ";

            switch (searchType) {

                case BY_POST_CATEGORY_DESC:
                    errorMsg += "PostCategoryDesc: '" + searchParam + "'. ";
                    break;

                case BY_POST_CATEGORY_NAME:
                    errorMsg += "PostCategoryName: '" + searchParam + "'. ";
                    break;
            }

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
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
