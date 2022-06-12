//package com.ntv.ntvcons_backend.services.FireBase;
//import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.FirebaseMessagingException;
//import com.google.firebase.messaging.Message;
//import com.google.firebase.messaging.Notification;
//import com.ntv.ntvcons_backend.entities.Note;
//import com.ntv.ntvcons_backend.entities.User;
//import com.ntv.ntvcons_backend.repositories.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class FirebaseMessagingService {
//
//    private final FirebaseMessaging firebaseMessaging;
//
//    @Autowired
//    UserRepository userRepository;
//
//    public FirebaseMessagingService(FirebaseMessaging firebaseMessaging) {
//        this.firebaseMessaging = firebaseMessaging;
//    }
//
//
//    public String sendNotification(Note note) throws FirebaseMessagingException {
//
//        User user = userRepository.getById(note.getUserId());
//
//            Notification notification = Notification
//                    .builder()
//                    .setTitle(note.getFunction())
//                    .setBody(note.getContent())
//                    .build();
//
//            Message message = Message
//                    .builder()
//                    .setToken(user.getFcmToken())
//                    .setNotification(notification)
//                    .putAllData(note.getData())
//                    .build();
//            return firebaseMessaging.send(message);
//
//    }
//
//}
