package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.ChatRoom;
import com.ntv.ntvcons_backend.entities.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ChatRepository extends PagingAndSortingRepository<Message, Long> {

    Page<Message> findByChatRoom(ChatRoom chatRoom, Pageable pageable);

    @Query(value = "select m from Message m where m.chatRoom = :chatRoom and ( m.userSeen is null or m.userSeen not like %:userid% )")
    Page<Message> findByChatRoomAndUserSeenNotContainingOrUserSeenNull(ChatRoom chatRoom, String userid, Pageable pageable);

}
