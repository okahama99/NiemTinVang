package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByName(String chatRoomName);

}
