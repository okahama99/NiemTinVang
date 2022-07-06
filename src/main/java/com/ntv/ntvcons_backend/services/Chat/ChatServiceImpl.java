package com.ntv.ntvcons_backend.services.Chat;

import com.ntv.ntvcons_backend.Enum.Status;
import com.ntv.ntvcons_backend.entities.ChatModels.ChatRoomModel;
import com.ntv.ntvcons_backend.entities.ChatModels.ListChatRoomModel;
import com.ntv.ntvcons_backend.entities.ChatModels.MessageModel;
import com.ntv.ntvcons_backend.entities.User;
import com.ntv.ntvcons_backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    UsersChatRoomRepository usersChatRoomRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public String saveUserChatRoom(String chatRoomId) {
        ChatRoom chatRoom2 = chatRoomRepository.findByName(chatRoomId);
        if(chatRoom2==null){
            String chatRoom = chatRoomId;
            String[] parts = chatRoom.split("_");
            String userId1 = parts[0];
            String userId2 = parts[1];
            ChatRoomModel chatRoom1 = new ChatRoomModel();
            chatRoom1.setChatRoomId(chatRoomId);
            User user1 = userRepository.findById(Long.parseLong(userId1)).get();
            User user2 = userRepository.findById(Long.parseLong(userId2)).get();
            UsersChatRoom usersChatRoom1 = createUsersChatRoom(user1);
            UsersChatRoom usersChatRoom2 = createUsersChatRoom(user2);
            ChatRoom room = createChatRoom(chatRoomId);
            ChatRoom room1 = chatRoomRepository.findByName(chatRoomId);
            usersChatRoom1.setChatRoom(room1);
            usersChatRoom2.setChatRoom(room1);
            usersChatRoomRepository.saveAndFlush(usersChatRoom1);
            usersChatRoomRepository.saveAndFlush(usersChatRoom2);
            return "Đã tạo phòng "+room.getName()+ " thành công.";
        }
        return "Phòng đã tồn tại";
    }

    @Override
    public boolean sendMessage(String chatroomName, Long userId, String message) {
        ChatRoom chatRoom = chatRoomRepository.findByName(chatroomName);
        List<UsersChatRoom> usersChatRooms = chatRoom.getUsersChatRooms();
        User user = userRepository.findById(userId).orElse(null);
        if(chatRoom!=null && user!=null){
            Message message1 = new Message();
            message1.setMessage(message);
            message1.setChatRoom(chatRoom);
            message1.setUser(user);
            message1.setStatus(Status.UNREADMESSAGE);
            messageRepository.saveAndFlush(message1);
            chatRoom.setUpdatedBy(user.getUserId());
            chatRoomRepository.saveAndFlush(chatRoom);
            for(int i = 0; i<usersChatRooms.size(); i++){
                if(usersChatRooms.get(i).getUser()!=user){
                    Notification notification = new Notification();
                    notification.setChatRoom(chatRoom);
                    notification.setDescription(user.getUsername() + " đã gửi cho bạn một tin nhắn mới.");
                    notification.setUser(usersChatRooms.get(i).getUser());
                    notificationRepository.saveAndFlush(notification);
                }
            }
        return true;
        }
        return false;
    }

    @Override
    public List<MessageModel> getMessage(String chatroomName, int pageNo, int pageSize) {
        ChatRoom chatRoom = chatRoomRepository.findByName(chatroomName);
        Pageable paging;
        paging = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").ascending());
        Page<Message> pagingResult = chatRepository.findByChatRoom(chatRoom, paging);
        if(pagingResult.hasContent()){
            double totalPage = Math.ceil((double)pagingResult.getTotalElements() / pageSize);
            Page<MessageModel> modelResult = pagingResult.map(new Converter<Message, MessageModel>() {
                @Override
                protected MessageModel doForward(Message message1) {
                    User user = userRepository.getById(message1.getUserId());
                    MessageModel messageModel = new MessageModel();
                    messageModel.setMessage(message1.getMessage());
                    messageModel.setAvatar(user.getAvatar());
                    messageModel.setDate(message1.getCreatedAt());
                    messageModel.setUserName(user.getUsername());
                    messageModel.setUserId(user.getUserId());
                    messageModel.setMessageId(message1.getId());
                    messageModel.setStatus(message1.getStatus());
                    messageModel.setTotalPage(totalPage);
                    return messageModel;
                }

                @Override
                protected Message doBackward(MessageModel maMessageModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        }else{
            return new ArrayList<>();
        }
    }

    @Override
    public boolean changeMessageStatus(Long messageId1, Long messageId2, String chatRoomName, String userId) {
        ChatRoom chatRoom = chatRoomRepository.findByName(chatRoomName);
        Long l = messageId2 - messageId1;
        if(l>0){
            for(long i = messageId1; i <= messageId2; i++){
                Message message = messageRepository.findByIdAndAndChatRoom(i, chatRoom);
                if(message!=null){
                    message.setStatus(Status.READMESSAGE);
                    if(message.getUserSeen() == null){
                        message.setUserSeen(userId);
                    }else {
                        String s = message.getUserSeen() + "_"+userId;
                        message.setUserSeen(s);
                    }
                    messageRepository.saveAndFlush(message);
                }
            }
        }
        else{
            Message message = messageRepository.findByIdAndAndChatRoom(messageId1, chatRoom);
            if(message!=null){
                message.setStatus(Status.READMESSAGE);
                messageRepository.saveAndFlush(message);
            }
        }
        return true;
    }

    @Override
    public List<MessageModel> getNewMessage(String chatRoomName, int pageNo, int pageSize, String userId) {
        ChatRoom chatRoom = chatRoomRepository.findByName(chatRoomName);
        Pageable paging;
        paging = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").ascending());
        Page<Message> pagingResult = chatRepository.findByChatRoomAndUserSeenNotContainingOrUserSeenNull(chatRoom, userId, paging);
        if(pagingResult.hasContent()){
            double totalPage = Math.ceil((double)pagingResult.getTotalElements() / pageSize);
            Page<MessageModel> modelResult = pagingResult.map(new Converter<Message, MessageModel>() {
                @Override
                protected MessageModel doForward(Message message1) {
                        User user = userRepository.getById(message1.getUserId());
                        MessageModel messageModel = new MessageModel();
                        messageModel.setMessage(message1.getMessage());
                        messageModel.setAvatar(user.getAvatar());
                        messageModel.setDate(message1.getCreatedAt());
                        messageModel.setUserName(user.getUsername());
                        messageModel.setUserId(user.getUserId());
                        messageModel.setMessageId(message1.getId());
                        messageModel.setStatus(message1.getStatus());
                        messageModel.setTotalPage(totalPage);
                        return messageModel;
                }

                @Override
                protected Message doBackward(MessageModel maMessageModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        }else{
            return new ArrayList<>();
        }
    }

    @Override
    public List<ListChatRoomModel> getListChatRoom(Long userId, int pageNo, int pageSize) {
        Pageable paging;
        paging = PageRequest.of(pageNo, pageSize);
        Page<UsersChatRoom> pagingResult = usersChatRoomRepository.findByUserOrderByChatRoomUpdatedAtDesc(userRepository.getById(userId), paging);
        if(pagingResult.hasContent()){
            double totalPage = Math.ceil((double)pagingResult.getTotalElements() / pageSize);
            Page<ListChatRoomModel> modelResult = pagingResult.map(new Converter<UsersChatRoom, ListChatRoomModel>() {
                @Override
                protected ListChatRoomModel doForward(UsersChatRoom usersChatRoom) {
                    User user = userRepository.getById(usersChatRoom.getUserId());
                    List<UsersChatRoom> usersChatRooms = usersChatRoom.getChatRoom().getUsersChatRooms();
                    ListChatRoomModel listChatRoomModel = new ListChatRoomModel();
                    Message message = messageRepository.findFirstByChatRoomOrderByCreateAtDesc(usersChatRoom.getChatRoom());
                    if(message!=null){
                        listChatRoomModel.setNewestMessage(message.getMessage());
                    }
                    for(int i = 0; i<usersChatRooms.size(); i++){
                        if(usersChatRooms.get(i).getUser()!=user){
                            listChatRoomModel.setUserName(usersChatRooms.get(i).getUser().getUsername());
                            listChatRoomModel.setAvatar(userProfileRepository.findByUser(usersChatRooms.get(i).getUser()).getAvatar());
                            listChatRoomModel.setUserId(usersChatRooms.get(i).getUser().getId());
                        }
                    }
                    listChatRoomModel.setTotalPage(totalPage);
                    return listChatRoomModel;
                }

                @Override
                protected UsersChatRoom doBackward(ListChatRoomModel chatRoomModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        }else{
            return new ArrayList<>();
        }
    }

    public ChatRoom createChatRoom(String chatRoomId){
        ChatRoom room = new ChatRoom();
        room.setName(chatRoomId);
        chatRoomRepository.saveAndFlush(room);
        return room;
    }
    public UsersChatRoom createUsersChatRoom(User user){
        UsersChatRoom usersChatRoom = new UsersChatRoom();
        usersChatRoom.setUser(user);
        usersChatRoomRepository.saveAndFlush(usersChatRoom);
        return usersChatRoom;
    }
}
