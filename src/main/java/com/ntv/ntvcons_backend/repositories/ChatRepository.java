package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.Enum.Status;
import com.ntv.ntvcons_backend.entities.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends PagingAndSortingRepository<ChatMessage, Long> {

    Page<ChatMessage> findByChatGroupIdAndStatus(Long chatGroupId, Status status, Pageable pageable);

    Page<ChatMessage> findByChatGroupIdAndStatusIsNot(Long chatRoom, Status status, Pageable pageable);

}
