//package com.ntv.ntvcons_backend.repositories;
//
//import com.ntv.ntvcons_backend.entities.Notification;
//import com.ntv.ntvcons_backend.entities.User;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface NotificationRepository extends JpaRepository<Notification, Long> {
//
//    Page<Notification> findByUser(User user, Pageable paging);
//
//    Notification findByNotificationTriggerId(Long notificationTriggerId);
//}
