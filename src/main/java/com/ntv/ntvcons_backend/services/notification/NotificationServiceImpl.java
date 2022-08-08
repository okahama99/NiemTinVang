//package com.ntv.ntvcons_backend.services.Notification;
//
//import com.google.common.base.Converter;
//import com.ntv.ntvcons_backend.entities.Notification;
//import com.ntv.ntvcons_backend.entities.NotificationModel.ShowNotificationModels;
//import com.ntv.ntvcons_backend.entities.User;
//import com.ntv.ntvcons_backend.repositories.NotificationRepository;
//import com.ntv.ntvcons_backend.repositories.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class NotificationServiceImpl implements NotificationService {
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    NotificationRepository notificationRepository;
//
//    @Override
//    public List<ShowNotificationModels> showNotiByUser(int pageNo, int pageSize, String sortBy, boolean sortType, Long userid) {
//        User user = userRepository.findById(userid).orElse(null);
//        Pageable paging;
//        if (sortType)
//        {
//            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
//        } else {
//            paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
//        }
//        Page<Notification> pagingResult = notificationRepository.findByUser(user, paging);
//        if (pagingResult.hasContent()) {
//            double totalPage = Math.ceil((double)pagingResult.getTotalElements() / pageSize);
//            Page<ShowNotificationModels> modelResult = pagingResult.map(new Converter<Notification, ShowNotificationModels>() {
//                @Override
//                protected ShowNotificationModels doForward(Notification notification) {
//                    ShowNotificationModels showNotificationModel = new ShowNotificationModels();
//                    showNotificationModel.setDescription(notification.getDescription());
//                    showNotificationModel.setId((notification.getId()));
//                    showNotificationModel.setUserName(notification.getUser().getUsername());
//                    showNotificationModel.setCreatedAt(notification.getCreatedAt());
//                    if (notification.getChatRoom()!=null) {
//                        showNotificationModel.setChatRoomName(notification.getChatRoom().getName()+ "_"+notification.getDescription().replace(" đã gửi cho bạn một tin nhắn mới.", "").trim());
//                    }
////                    if (notification.getReport()!=null) {
////                        showNotificationModel.setReportId(notification.getReport().getId());
////                    }
//
//                    showNotificationModel.setTotalPage(totalPage);
//                    return showNotificationModel;
//                }
//
//                @Override
//                protected Notification doBackward(ShowNotificationModels showNotificationModel) {
//                    return null;
//                }
//            });
//            return modelResult.getContent();
//        } else {
//            return null;
//        }
//    }
//}
