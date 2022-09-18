package com.ntv.ntvcons_backend.services.conversation;

import com.ntv.ntvcons_backend.dtos.user.UserReadDTO;
import com.ntv.ntvcons_backend.entities.Conversation;
import com.ntv.ntvcons_backend.entities.ConversationModels.ShowConversationModel;
import com.ntv.ntvcons_backend.entities.Message;
import com.ntv.ntvcons_backend.repositories.ConversationRepository;
import com.ntv.ntvcons_backend.repositories.MessageRepository;
import com.ntv.ntvcons_backend.services.user.UserService;
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
    private UserService userService;

    @Override
    public List<ShowConversationModel> getUserConversations(Long userId) throws Exception{
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

    public ShowConversationModel setupConversationModelData(Conversation conversation, Long userId) throws Exception {
        if (userId == null) {
            ShowConversationModel model = new ShowConversationModel();

            model.setConversationId(conversation.getConversationId());
            model.setUser1Id(conversation.getUser1Id());
            model.setUser2Id(conversation.getUser2Id());
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
            model.setUser1Id(conversation.getUser1Id());
            model.setUser2Id(conversation.getUser2Id());

//            User user = userRepository.findByUserIdAndStatusNotIn(userId, N_D_S_STATUS_LIST).orElse(null);
//            model.setName(user.getFullName());

            UserReadDTO userDTO = userService.getDTOById(userId);
            String avatarLink = "Không có avatar";
            if (userDTO.getFile() != null){
                avatarLink = userDTO.getFile().getFileLink();
            }
            model.setAvatar(avatarLink);
            model.setName(userDTO.getFullName());


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
        Conversation existed1 = conversationRepository.findByUser1IdAndAndUser2Id(currentUserId, targetUserId);
        Conversation existed2 = conversationRepository.findByUser1IdAndAndUser2Id(targetUserId, currentUserId);
        if(existed1 == null && existed2 == null){
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

        if(existed1 != null){
            Message newMessage = new Message();
            newMessage.setConversationId(existed1.getConversationId());
            newMessage.setSenderId(currentUserId);
            newMessage.setSendTime(LocalDateTime.now());
            newMessage.setMessage(message);
            Message createdNewMessage = messageRepository.saveAndFlush(newMessage);

            if (createdNewMessage == null) {
                return null;
            }
            return createdNewMessage.getMessageId();
        }

        if(existed2 != null){
            Message newMessage = new Message();
            newMessage.setConversationId(existed2.getConversationId());
            newMessage.setSenderId(currentUserId);
            newMessage.setSendTime(LocalDateTime.now());
            newMessage.setMessage(message);
            Message createdNewMessage = messageRepository.saveAndFlush(newMessage);

            if (createdNewMessage == null) {
                return null;
            }
            return createdNewMessage.getMessageId();
        }

        return null;
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
