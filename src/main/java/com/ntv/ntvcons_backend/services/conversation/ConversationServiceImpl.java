package com.ntv.ntvcons_backend.services.conversation;

import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.entities.Conversation;
import com.ntv.ntvcons_backend.entities.ConversationModels.ShowConversationModel;
import com.ntv.ntvcons_backend.entities.Message;
import com.ntv.ntvcons_backend.entities.User;
import com.ntv.ntvcons_backend.repositories.ConversationRepository;
import com.ntv.ntvcons_backend.repositories.MessageRepository;
import com.ntv.ntvcons_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConversationServiceImpl implements ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    private final List<Status> N_D_S_STATUS_LIST = Status.getAllNonDefaultSearchStatus();

    @Override
    public List<ShowConversationModel> getUserConversations(Long userId) {
        //Lấy list conversation của user
        List<Conversation> listConversation1 = conversationRepository.findAllByUser1Id(userId);
        List<Conversation> listConversation2 = conversationRepository.findAllByUser2Id(userId);

        // Setup response
        List<ShowConversationModel> returnList = new ArrayList<>();
        if (listConversation1 == null && listConversation2 == null) {
            return null;
        } else if (listConversation1 != null && listConversation2 == null) {
            for (Conversation conversation : listConversation1) {
                ShowConversationModel model = setupConversationModelData(conversation, conversation.getUser2Id());
                returnList.add(model);
            }
        } else if (listConversation1 == null && listConversation2 != null) {
            for (Conversation conversation : listConversation2) {
                ShowConversationModel model = setupConversationModelData(conversation, conversation.getUser1Id());
                returnList.add(model);
            }
        } else {
            for (Conversation conversation : listConversation1) {
                ShowConversationModel model = setupConversationModelData(conversation, conversation.getUser2Id());
                returnList.add(model);
            }
            for (Conversation conversation : listConversation2) {
                ShowConversationModel model = setupConversationModelData(conversation, conversation.getUser1Id());
                returnList.add(model);
            }
        }
        return returnList;
    }

    public ShowConversationModel setupConversationModelData(Conversation conversation, Long userId) {
        if (userId == null) {
            ShowConversationModel model = new ShowConversationModel();

            model.setName(conversation.getClientName());
            model.setAvatar(null); // khi FE nhận null sẽ set avatar mặc định

            Message message = messageRepository.findTopByOrderByConversationIdDesc();
            model.setLastMessage(message.getMessage());
            model.setLastMessageTime(message.getSendTime());
            return model;
        } else {
            ShowConversationModel model = new ShowConversationModel();

            User user = userRepository.findByUserIdAndStatusNotIn(userId, N_D_S_STATUS_LIST).orElse(null);
            model.setName(user.getFullName());
//            model.setAvatar(user.getAvatar()); //TODO : thêm avatar

            Message message = messageRepository.findTopByOrderByConversationIdDesc();
            model.setLastMessage(message.getMessage());
            model.setLastMessageTime(message.getSendTime());
            return model;
        }
    }

    @Override
    public boolean createConversationForAuthenticated(Long currentUserId, Long targetUserId, String message, MultipartFile file) throws IOException {
        Conversation conversation = new Conversation();
        conversation.setUser1Id(currentUserId);
        conversation.setUser2Id(targetUserId);
//        conversation.setCreatedAt(LocalDateTime.now());
//        conversation.setCreatedBy(currentUserId);
        Conversation createdConversation = conversationRepository.saveAndFlush(conversation);

        if (createdConversation == null) {
            return false;
        }

        Message messageEntity = new Message();
        messageEntity.setConversationId(createdConversation.getConversationId());
        messageEntity.setSenderId(currentUserId);
        messageEntity.setMessage(message);
//        messageEntity.setCreatedAt(LocalDateTime.now());
//        messageEntity.setCreatedBy(currentUserId);
        if (file != null) {
            messageEntity.setFileName(file.getName());
            messageEntity.setFileType(file.getContentType());
            messageEntity.setData(file.getBytes());
        }
        messageRepository.saveAndFlush(messageEntity);
        return true;
    }

    @Override
    public boolean createConversationForUnauthenticated(String clientIp, String clientName, String message, MultipartFile file) throws IOException {
        Conversation conversation = new Conversation();
        conversation.setUser1Id(null);
        conversation.setUser2Id(null);
        conversation.setIpAddress(clientIp);
        conversation.setClientName(clientName);
//        conversation.setCreatedAt(LocalDateTime.now());
        Conversation createdConversation = conversationRepository.saveAndFlush(conversation);

        if (createdConversation == null) {
            return false;
        }

        Message messageEntity = new Message();
        messageEntity.setConversationId(createdConversation.getConversationId());
        messageEntity.setSenderId(null);
        messageEntity.setMessage(message);
//        messageEntity.setCreatedAt(LocalDateTime.now());
        if (file != null) {
            messageEntity.setFileName(file.getName());
            messageEntity.setFileType(file.getContentType());
            messageEntity.setData(file.getBytes());
        }
        messageRepository.saveAndFlush(messageEntity);
        return true;
    }

    @Override
    public boolean setConsultantForChat(Long userId, Long conversationId, String message, MultipartFile file) throws IOException {
        Conversation conversation = conversationRepository.findById(conversationId).orElse(null);
        conversation.setUser1Id(userId);
//        conversation.setCreatedAt(LocalDateTime.now());
//        conversation.setCreatedBy(userId);
        Conversation createdConversation = conversationRepository.saveAndFlush(conversation);

        if (createdConversation == null) {
            return false;
        }

        Message messageEntity = new Message();
        messageEntity.setConversationId(createdConversation.getConversationId());
        messageEntity.setSenderId(userId);
        messageEntity.setMessage(message);
//        messageEntity.setCreatedAt(LocalDateTime.now());
//        messageEntity.setCreatedBy(userId);
        if (file != null) {
            messageEntity.setFileName(file.getName());
            messageEntity.setFileType(file.getContentType());
            messageEntity.setData(file.getBytes());
        }
        messageRepository.saveAndFlush(messageEntity);
        return true;
    }
}
