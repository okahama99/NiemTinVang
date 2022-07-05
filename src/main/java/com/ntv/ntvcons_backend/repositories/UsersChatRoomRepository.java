package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.ChatRoom;
import com.ntv.ntvcons_backend.entities.User;
import com.ntv.ntvcons_backend.entities.UsersChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersChatRoomRepository extends JpaRepository<UsersChatRoom, Long> {
    List<UsersChatRoom> findByChatRoom(ChatRoom chatRoom);

    List<UsersChatRoom> findByUser(User user);

    Page<UsersChatRoom> findByUserOrderByChatRoomUpdatedAtDesc(User user, Pageable pageable);
}
