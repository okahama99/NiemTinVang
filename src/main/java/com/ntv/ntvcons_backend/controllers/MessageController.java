package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.constants.FileType;
import com.ntv.ntvcons_backend.constants.SearchType;
import com.ntv.ntvcons_backend.dtos.ErrorResponse;
import com.ntv.ntvcons_backend.entities.MessageModels.ShowMessageModel;
import com.ntv.ntvcons_backend.services.message.MessageService;
import com.ntv.ntvcons_backend.services.misc.FileCombineService;
import com.ntv.ntvcons_backend.utils.JwtUtil;
import com.ntv.ntvcons_backend.utils.MiscUtil;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MiscUtil miscUtil;

    @Autowired
    private FileCombineService fileCombineService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping(value = "/v1/getByConversationId", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getByConversationId(
            @RequestParam Long conversationId,
            @RequestParam int pageNo,
            @RequestParam int pageSize,
            @RequestParam @Parameter(example = "messageId") String sortBy,
            @RequestParam boolean sortTypeAsc){
        try {
            Pageable paging = miscUtil.makePaging(pageNo, pageSize, sortBy, sortTypeAsc);
            List<ShowMessageModel> list = messageService.getByConversationId(conversationId, paging);

            return ResponseEntity.ok().body(list);
        } catch (NumberFormatException nFE) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(
                            "Invalid parameter type for conversationId: '" + conversationId
                                    + "'. Expecting parameter of type: Long",
                            nFE.getMessage()));
        } catch (IllegalArgumentException iAE) {
            /* Catch invalid searchType */
            return ResponseEntity.badRequest().body(
                    new ErrorResponse("Invalid parameter given", iAE.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = "Error searching for Message with ConversationId : " + conversationId;

            return ResponseEntity.internalServerError().body(new ErrorResponse(errorMsg, e.getMessage()));
        }
    }

    @PreAuthorize("hasAnyAuthority('24','54','14','34','44','4')")
    @PostMapping(value = "/v1/sendMessageAuthenticated", produces = "application/json;charset=UTF-8", consumes = "multipart/form-data")
    public ResponseEntity<Object> sendMessageAuthenticated(
            @RequestParam Long conversationId,
            @RequestParam String message,
            @RequestPart(required = false) @Size(min = 1) List<MultipartFile> file,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) throws Exception {
        String jwt = jwtUtil.getAndValidateJwt(token);
        Long userId = jwtUtil.getUserIdFromJWT(jwt);
        if (userId == null)
            throw new IllegalArgumentException("Invalid jwt token.");


        Long messageId = messageService
                .sendMessageAuthenticated(userId, conversationId, message);

        if (messageId != null) {
            if (file != null) {
                fileCombineService.saveAllFileInDBAndFirebase(
                        file, FileType.MESSAGE_FILE, messageId, EntityType.MESSAGE_ENTITY, userId);
            }

            return ResponseEntity.ok().body("Gửi thành công.");
        }
        return ResponseEntity.badRequest().body("Gửi thất bại.");
    }

    @PostMapping(value = "/v1/sendMessageUnauthenticated", produces = "application/json;charset=UTF-8", consumes = "multipart/form-data")
    public ResponseEntity<Object> sendMessageUnauthenticated(
            @RequestParam String ipAddress,
            @RequestParam Long conversationId,
            @RequestParam String message,
            @RequestPart(required = false) @Size(min = 1) List<MultipartFile> file) throws Exception {

        Long messageId = messageService
                .sendMessageUnauthenticated(ipAddress, conversationId, message);

        if (messageId != null) {
            if (file != null) {
                fileCombineService.saveAllFileInDBAndFirebase(
                        file, FileType.MESSAGE_FILE, messageId, EntityType.MESSAGE_ENTITY, messageId);
            }

            return ResponseEntity.ok().body("Gửi thành công.");
        }
        return ResponseEntity.badRequest().body("Gửi thất bại.");
    }

    @PostMapping(value = "/v1/seenMessageUnauthenticated", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> seenMessageUnauthenticated(
            @RequestParam String ipAddress,
            @RequestParam Long conversationId) {
        boolean result = messageService.seenMessageUnauthenticated(ipAddress, conversationId);

        if (result) {
            return ResponseEntity.ok().body("Seen thành công.");
        }

        return ResponseEntity.badRequest().body("Seen thất bại.");
    }

    @PreAuthorize("hasAnyAuthority('24','54','14','34','44','4')")
    @PostMapping(value = "/v1/seenMessageAuthenticated", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> seenMessageAuthenticated(
            @RequestParam Long conversationId,
            @RequestHeader(name = "Authorization") @Parameter(hidden = true) String token) throws Exception {

        String jwt = jwtUtil.getAndValidateJwt(token);
        Long userId = jwtUtil.getUserIdFromJWT(jwt);
        if (userId == null)
            throw new IllegalArgumentException("Invalid jwt token.");

        boolean result = messageService.seenMessageAuthenticated(userId, conversationId);

        if (result) {
            return ResponseEntity.ok().body("Seen thành công.");
        }

        return ResponseEntity.badRequest().body("Seen thất bại.");
    }
}
