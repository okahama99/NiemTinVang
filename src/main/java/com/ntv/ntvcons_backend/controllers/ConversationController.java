package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.entities.ConversationModels.ShowConversationModel;
import com.ntv.ntvcons_backend.services.conversation.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/conversation")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @PreAuthorize("hasAnyAuthority('24','54','14','34','44','4')")
    @PostMapping(value = "/v1/getUserConversations", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> getUserConversations(@RequestParam Long tokenUserId) {
        List<ShowConversationModel> result = conversationService.getUserConversations(tokenUserId);
        return ResponseEntity.ok().body(result);
    }

    @PreAuthorize("hasAnyAuthority('24','54','14','34','44','4')")
    @PostMapping(value = "/v1/createConversationForAuthenticated", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createConversationForAuthenticated(@RequestParam Long currentUserId,
                                                                     @RequestParam Long targetUserId,
                                                                     @RequestParam String message,
                                                                     @RequestParam MultipartFile file) throws IOException {
        boolean result = conversationService.createConversationForAuthenticated(currentUserId, targetUserId, message, file);
        if (result) {
            return ResponseEntity.ok().body("Tạo thành công.");
        }
        return ResponseEntity.badRequest().body("Tạo thất bại.");
    }

    @PostMapping(value = "/v1/createConversationForUnauthenticated", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> createConversationForUnauthenticated(@RequestParam String clientIp,
                                                                       @RequestParam String clientName,
                                                                       @RequestParam String message,
                                                                       @RequestParam MultipartFile file) throws IOException {
        boolean result = conversationService.createConversationForUnauthenticated(clientIp, clientName, message, file);
        if (result) {
            return ResponseEntity.ok().body("Tạo thành công.");
        }
        return ResponseEntity.badRequest().body("Tạo thất bại.");
    }

    @PreAuthorize("hasAnyAuthority('34')")
    @PutMapping(value = "/v1/setConsultantForChat", produces = "application/json;charset=UTF-8")
    public ResponseEntity<Object> setConsultantForChat(@RequestParam Long userId,
                                                       @RequestParam Long conversationId,
                                                       @RequestParam String message,
                                                       @RequestParam MultipartFile file) throws IOException {
        boolean result = conversationService.setConsultantForChat(userId, conversationId, message, file);

        if (result) {
            return ResponseEntity.ok().body("Cập nhật thành công.");
        }

        return ResponseEntity.badRequest().body("Cập nhật thất bại.");
    }
}