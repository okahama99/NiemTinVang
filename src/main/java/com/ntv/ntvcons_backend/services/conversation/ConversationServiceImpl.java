package com.ntv.ntvcons_backend.services.conversation;

import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.entities.Conversation;
import com.ntv.ntvcons_backend.entities.ConversationModels.ShowConversationModel;
import com.ntv.ntvcons_backend.entities.Message;
import com.ntv.ntvcons_backend.entities.User;
import com.ntv.ntvcons_backend.repositories.ConversationRepository;
import com.ntv.ntvcons_backend.repositories.MessageRepository;
import com.ntv.ntvcons_backend.repositories.UserRepository;
import com.ntv.ntvcons_backend.services.externalFile.ExternalFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private ExternalFileService externalFileService;

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

            model.setConversationId(conversation.getConversationId());
            model.setName(conversation.getClientName());
            model.setAvatar(null); // khi FE nhận null sẽ set avatar mặc định

            List<Message> listMsg = messageRepository.findAllByConversationIdAndSenderId(conversation.getConversationId(), userId);
            if(!listMsg.isEmpty())
            {
                Message message = listMsg.get(listMsg.size()-1);
                model.setLastMessage(message.getMessage());
                model.setLastMessageTime(message.getSendTime());
            }
            return model;
        } else {
            ShowConversationModel model = new ShowConversationModel();
            model.setConversationId(conversation.getConversationId());

            User user = userRepository.findByUserIdAndStatusNotIn(userId, N_D_S_STATUS_LIST).orElse(null);
            model.setName(user.getFullName());
//            model.setAvatar(user.getAvatar()); //TODO : thêm avatar nếu ông xong nha Thành :v

            List<Message> listMsg = messageRepository.findAllByConversationIdAndSenderId(conversation.getConversationId(), userId);
            if(!listMsg.isEmpty())
            {
                Message message = listMsg.get(listMsg.size()-1);
                model.setLastMessage(message.getMessage());
                model.setLastMessageTime(message.getSendTime());
            }
            return model;
        }
    }

    @Override
    public Long createConversationForAuthenticated(Long currentUserId, Long targetUserId, String message) {
        Conversation conversation = new Conversation();
        conversation.setUser1Id(currentUserId);
        conversation.setUser2Id(targetUserId);
        Conversation createdConversation = conversationRepository.saveAndFlush(conversation);

        Message messageEntity = new Message();
        messageEntity.setConversationId(createdConversation.getConversationId());
        messageEntity.setSenderId(currentUserId);
        messageEntity.setMessage(message);
        messageEntity.setSendTime(LocalDateTime.now());
        Message createdMessage = messageRepository.saveAndFlush(messageEntity);

        if (createdConversation == null || createdMessage == null) {
            return null;
        }
        return createdMessage.getMessageId();
    }

    @Override
    public Long createConversationForUnauthenticated(String clientIp, String clientName, String message) {
        Conversation conversation = new Conversation();
        conversation.setUser1Id(null);
        conversation.setUser2Id(null);
        conversation.setIpAddress(clientIp);
        conversation.setClientName(clientName);
        Conversation createdConversation = conversationRepository.saveAndFlush(conversation);

        Message messageEntity = new Message();
        messageEntity.setConversationId(createdConversation.getConversationId());
        messageEntity.setSenderId(null);
        messageEntity.setMessage(message);
        messageEntity.setSendTime(LocalDateTime.now());
        Message createdMessage = messageRepository.saveAndFlush(messageEntity);

        if (createdConversation == null || createdMessage == null) {
            return null;
        }
        return createdMessage.getMessageId();
    }

    @Override
    public Long setConsultantForChat(Long userId, Long conversationId, String message) {
        Conversation conversation = conversationRepository.findById(conversationId).orElse(null);
        conversation.setUser1Id(userId);
        Conversation createdConversation = conversationRepository.saveAndFlush(conversation);

        Message messageEntity = new Message();
        messageEntity.setConversationId(createdConversation.getConversationId());
        messageEntity.setSenderId(userId);
        messageEntity.setMessage(message);
        messageEntity.setSendTime(LocalDateTime.now());
        Message createdMessage = messageRepository.saveAndFlush(messageEntity);

        if (createdConversation == null || createdMessage == null) {
            return null;
        }
        return createdMessage.getMessageId();
    }
}
