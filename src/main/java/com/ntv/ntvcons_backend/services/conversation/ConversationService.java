package com.ntv.ntvcons_backend.services.conversation;

import com.ntv.ntvcons_backend.entities.ConversationModels.ShowConversationModel;

import java.util.List;

public interface ConversationService {
    List<ShowConversationModel> getUserConversations(Long userId) throws Exception;

    Long createConversationForAuthenticated(Long currentUserId, Long targetUserId, String message);

    Long createConversationForUnauthenticated(String clientIp, String clientName, String message);

    Long setConsultantForChat(Long userId, Long conversationId, String message);
}
