package com.ntv.ntvcons_backend.services.message;

import com.ntv.ntvcons_backend.entities.MessageModels.ShowMessageModel;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface MessageService {
    List<ShowMessageModel> getByConversationIdAuthenticated(Long userId, Long conversationId, Pageable paging);

    List<ShowMessageModel> getByConversationIdUnauthenticated(String ipAddress, Long conversationId, Pageable paging);

    Long sendMessageAuthenticated(Long userId, Long conversationId, String message);

    Long sendMessageUnauthenticated(String ipAddress, Long conversationId, String message);

    boolean seenMessageAuthenticated(Long userId, Long conversationId);

    boolean seenMessageUnauthenticated(String ipAddress, Long conversationId);

    boolean existsById(Long messageId);
}
