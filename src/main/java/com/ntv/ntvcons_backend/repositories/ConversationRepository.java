package com.ntv.ntvcons_backend.repositories;

import com.ntv.ntvcons_backend.entities.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    List<Conversation> findAllByUser1Id(Long userId);

    List<Conversation> findAllByUser2Id(Long userId);

    List<Conversation> findAllByIpAddress(String ipAddress);

    Conversation findByUser1IdAndAndUser2Id(Long userId1, Long userId2);

}
