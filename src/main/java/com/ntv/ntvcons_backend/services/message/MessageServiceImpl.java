package com.ntv.ntvcons_backend.services.message;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.entities.Conversation;
import com.ntv.ntvcons_backend.entities.Message;
import com.ntv.ntvcons_backend.entities.MessageModels.ShowMessageModel;
import com.ntv.ntvcons_backend.repositories.ConversationRepository;
import com.ntv.ntvcons_backend.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Override
    public List<ShowMessageModel> getByConversationIdAuthenticated(Long userId, Long conversationId, Pageable paging) {
        List<Conversation> combinedList = new ArrayList<>();
        List<Conversation> list1 = conversationRepository.findAllByUser1Id(userId);
        List<Conversation> list2 = conversationRepository.findAllByUser2Id(userId);

        if (list1 == null && list2 == null) {
            return null;
        } else if (list1 != null && list2 == null) {
            for (Conversation conversation : list1) {
                combinedList.add(conversation);
            }
        } else if (list1 == null && list2 != null) {
            for (Conversation conversation : list2) {
                combinedList.add(conversation);
            }
        } else {
            for (Conversation conversation : list1) {
                combinedList.add(conversation);
            }
            for (Conversation conversation : list2) {
                combinedList.add(conversation);
            }
        }

        List<ShowMessageModel> temp;
        List<ShowMessageModel> result = new ArrayList<>();
        for (Conversation conversation : combinedList) {
            Page<Message> pagingResult = messageRepository.findAllByConversationId(conversation.getConversationId(), paging);
            temp = getShowMessageModels(pagingResult);
            for (ShowMessageModel model : temp) {
                result.add(model);
            }
        }
        return result;
    }

    @Override
    public List<ShowMessageModel> getByConversationIdUnauthenticated(String ipAddress, Long conversationId, Pageable paging) {
        List<Conversation> list1 = conversationRepository.findAllByIpAddress(ipAddress);

        if (list1 != null) {
            List<ShowMessageModel> temp;
            List<ShowMessageModel> result = new ArrayList<>();
            for (Conversation conversation : list1) {
                Page<Message> pagingResult = messageRepository.findAllByConversationId(conversation.getConversationId(), paging);
                temp = getShowMessageModels(pagingResult);
                for (ShowMessageModel model : temp) {
                    result.add(model);
                }
            }
            return result;
        }
        return null;
    }

    @Override
    public boolean sendMessage(Long userId, Long conversationId, String message, MultipartFile file) throws IOException {
        Message messageEntity = new Message();
        messageEntity.setConversationId(conversationId);
        messageEntity.setSenderId(userId);
        messageEntity.setMessage(message);
        messageEntity.setCreatedAt(LocalDateTime.now());
        messageEntity.setCreatedBy(userId);
        if (file != null) {
            messageEntity.setFileName(file.getName());
            messageEntity.setFileType(file.getContentType());
            messageEntity.setData(file.getBytes());
        }
        messageRepository.saveAndFlush(messageEntity);
        return true;
    }

    @Override
    public boolean seenMessageAuthenticated(Long userId, Long conversationId) {
        List<Message> listMessage = messageRepository.findAllByConversationIdAndSenderId(conversationId, userId);
        if(listMessage!=null){
            for (Message message : listMessage) {
                message.setSeen(true);
                message.setUpdatedAt(LocalDateTime.now());
                message.setUpdatedBy(userId);
                messageRepository.saveAndFlush(message);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean seenMessageUnauthenticated(String ipAddress, Long conversationId) {
        List<Message> listMessage = messageRepository.findAllByConversationIdAndSenderIp(conversationId, ipAddress);
        if(listMessage!=null){
            for (Message message : listMessage) {
                message.setSeen(true);
                message.setUpdatedAt(LocalDateTime.now());
                messageRepository.saveAndFlush(message);
            }
            return true;
        }
        return false;
    }

    private List<ShowMessageModel> getShowMessageModels(Page<Message> pagingResult) {
        if (pagingResult.hasContent()) {
            int totalPage = pagingResult.getTotalPages();

            Page<ShowMessageModel> modelResult = pagingResult.map(new Converter<Message, ShowMessageModel>() {

                @Override
                protected ShowMessageModel doForward(Message message) {
                    ShowMessageModel model = new ShowMessageModel();

                    model.setMessageId(message.getMessageId());
                    model.setConversationId(message.getConversationId());
                    model.setSenderId(message.getSenderId());
                    model.setSenderIp(message.getSenderIp());
                    model.setMessage(message.getMessage());
                    model.setFileName(message.getFileName());
                    model.setFileType(message.getFileType());
                    model.setSeen(message.getSeen());
                    model.setData(message.getData());
                    model.setSendTime(message.getSendTime());

                    model.setCreatedAt(message.getCreatedAt());
                    model.setCreatedBy(message.getCreatedBy());
                    model.setUpdatedAt(message.getCreatedAt());
                    model.setUpdatedBy(message.getUpdatedBy());
                    model.setTotalPage(totalPage);
                    return model;
                }

                @Override
                protected Message doBackward(ShowMessageModel showMessageModel) {
                    return null;
                }
            });
            return modelResult.getContent();
        } else {
            return new ArrayList<ShowMessageModel>();
        }
    }
}
