//package com.ntv.ntvcons_backend.repositories;
//
//import com.ntv.ntvcons_backend.Enum.Status;
//import com.ntv.ntvcons_backend.entities.ChatMember;
//import com.ntv.ntvcons_backend.entities.User;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {
//    List<ChatMember> findByChatGroupIdAndStatus(Long chatGroupId, Status status);
//
//    List<ChatMember> findByUserAndStatus(User user, Status status);
//
//    Page<ChatMember> findByUserOrderByChatRoomUpdatedAtDescAndStatus(User user, Pageable paging, Status status);
//
//    Page<ChatMember> findByStatusAndMemberIdOrderByUpdatedAtDesc(Status status, Long userId, Pageable paging);
//}
