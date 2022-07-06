package com.ntv.ntvcons_backend.services.Chat;


import com.ntv.ntvcons_backend.entities.ChatModels.ListChatRoomModel;
import com.ntv.ntvcons_backend.entities.ChatModels.MessageModel;

import java.util.List;

public interface ChatService {
    String saveUserChatRoom(String chatRoomId);
    boolean sendMessage(String chatroomName, Long userId, String message);

    List<MessageModel> getMessage(String chatroomName, int pageNo, int pageSize);

    boolean changeMessageStatus(Long messageId1, Long messageId2, String chatRoomName, String userId);
    List<MessageModel> getNewMessage(String chatRoomName, int pageNo, int pageSize, String userId);
    List<ListChatRoomModel> getListChatRoom(Long userId, int pageNo, int pageSize);
}
