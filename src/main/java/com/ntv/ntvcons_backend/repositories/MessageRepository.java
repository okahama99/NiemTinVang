package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Message findTopByOrderByConversationIdDesc();

    Page<Message> findAllByConversationId(Long conversationId, Pageable pageable);

    List<Message> findAllByConversationIdAndSenderIp(Long conversationId, String senderIp);

    List<Message> findAllByConversationIdAndSenderId(Long conversationId, Long senderId);

    boolean existsByMessageId(
            long messageId);
}
