//package com.ntv.ntvcons_backend.services.FireBase;
//
//
//import com.google.api.core.ApiFuture;
//import com.google.cloud.firestore.*;
//import com.google.firebase.cloud.FirestoreClient;
//import com.ntv.ntvcons_backend.entities.UserModels.UserFCMToken;
//import org.springframework.stereotype.Service;
//
//import java.util.concurrent.ExecutionException;
//
//@Service
//public class CRUDUserFireBaseService {
//
//
//    public static String saveUserDetails(UserFCMToken userFCMToken) throws InterruptedException, ExecutionException {
//        Firestore dbFirestore = FirestoreClient.getFirestore();
//        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("users").document(userFCMToken.getUserid().toString()).set(userFCMToken);
//        return collectionsApiFuture.get().getUpdateTime().toString();
//    }
//
////    public static List<ChatRoomModel> getChatRoom(String chatRoomId) throws InterruptedException, ExecutionException {
////        Firestore dbFirestore = FirestoreClient.getFirestore();
////        CollectionReference chatRoom = dbFirestore.collection("ChatRoom/"+chatRoomId +"/chats");
////        ApiFuture<QuerySnapshot> future = chatRoom.whereEqualTo("chatRoomId", chatRoomId).get();
////        QuerySnapshot queryDocumentSnapshots = future.get();
////        return queryDocumentSnapshots.getDocuments()
////                .stream()
////                .map(d -> d.toObject(ChatRoomModel.class))
////                .collect(Collectors.toList());
////    }
//
//    public static String updatePatientDetails(UserFCMToken userFCMToken) throws InterruptedException, ExecutionException {
//        Firestore dbFirestore = FirestoreClient.getFirestore();
//        ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("users").document(userFCMToken.getUserid().toString()).set(userFCMToken);
//        return collectionsApiFuture.get().getUpdateTime().toString();
//    }
//
//    public static String deletePatient(UserFCMToken userFCMToken) {
//        Firestore dbFirestore = FirestoreClient.getFirestore();
//        ApiFuture<WriteResult> writeResult = dbFirestore.collection("users").document(userFCMToken.getUserid().toString()).delete();
//        return "Đã xóa.";
//    }
//
//}
