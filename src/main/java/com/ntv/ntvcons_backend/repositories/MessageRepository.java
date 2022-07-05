package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.ChatRoom;
import com.ntv.ntvcons_backend.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatRoom(ChatRoom chatRoom);
    Message findFirstByChatRoomOrderByCreateAtDesc(ChatRoom chatRoom);
    Message findByIdAndAndChatRoom(Long id, ChatRoom chatRoom);
}
