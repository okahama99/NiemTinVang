package com.ntv.ntvcons_backend.services.conversation;

import com.ntv.ntvcons_backend.entities.ConversationModels.ShowConversationModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ConversationService {
    List<ShowConversationModel> getUserConversations(Long userId);

    boolean createConversationForAuthenticated(Long currentUserId, Long targetUserId, String message, MultipartFile file) throws IOException;

    boolean createConversationForUnauthenticated(String clientIp, String clientName, String message, MultipartFile file) throws IOException;

    boolean setConsultantForChat(Long userId, Long conversationId, String message, MultipartFile file) throws IOException;
}
