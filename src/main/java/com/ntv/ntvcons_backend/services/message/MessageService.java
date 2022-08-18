package com.ntv.ntvcons_backend.services.message;

import com.ntv.ntvcons_backend.entities.MessageModels.ShowMessageModel;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MessageService {
    List<ShowMessageModel> getByConversationIdAuthenticated(Long userId, Long conversationId, Pageable paging);

    List<ShowMessageModel> getByConversationIdUnauthenticated(String ipAddress, Long conversationId, Pageable paging);

    boolean sendMessageAuthenticated(Long userId, Long conversationId, String message, MultipartFile file) throws IOException;

    boolean sendMessageUnauthenticated(String ipAddress, Long conversationId, String message, MultipartFile file) throws IOException;

    boolean seenMessageAuthenticated(Long userId, Long conversationId);

    boolean seenMessageUnauthenticated(String ipAddress, Long conversationId);
}
