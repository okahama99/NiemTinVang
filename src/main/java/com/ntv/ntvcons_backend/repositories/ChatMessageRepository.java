//package com.ntv.ntvcons_backend.repositories;
//
//import com.ntv.ntvcons_backend.Enum.Status;
//import com.ntv.ntvcons_backend.entities.ChatGroup;
//import com.ntv.ntvcons_backend.entities.ChatMessage;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
//    List<ChatMessage> findByChatGroupIdAndStatus(ChatGroup chatGroup, Status status);
//    ChatMessage findFirstByChatGroupIdOrderByCreatedAtDesc(Long chatGroupId);
//    ChatMessage findByIdAndAndChatGroupId(Long id, Long chatGroupId);
//}
