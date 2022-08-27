package com.ntv.ntvcons_backend.services.message;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.constants.Status;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileReadDTO;
import com.ntv.ntvcons_backend.entities.*;
import com.ntv.ntvcons_backend.entities.MessageModels.MessageFileModel;
import com.ntv.ntvcons_backend.entities.MessageModels.ShowMessageModel;
import com.ntv.ntvcons_backend.repositories.*;
import com.ntv.ntvcons_backend.services.entityWrapper.EntityWrapperService;
import com.ntv.ntvcons_backend.services.externalFileEntityWrapperPairing.ExternalFileEntityWrapperPairingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private EntityWrapperService entityWrapperService;
    @Autowired
    private ExternalFileEntityWrapperPairingService eFEWPairingService;

    private final EntityType ENTITY_TYPE = EntityType.MESSAGE_ENTITY;
    private final List<Status> N_D_S_STATUS_LIST = Status.getAllNonDefaultSearchStatus();

    @Override
    public List<ShowMessageModel> getByConversationIdAuthenticated(
            Long userId, Long conversationId, Pageable paging) throws Exception {
        List<Conversation> combineConversationList = new ArrayList<>();
        List<Conversation> user1ConversationList = conversationRepository.findAllByUser1Id(userId);
        List<Conversation> user2ConversationList = conversationRepository.findAllByUser2Id(userId);

        if (user1ConversationList == null && user2ConversationList == null) {
            return null;
        } else if (user1ConversationList != null
                && user2ConversationList == null) {
            combineConversationList.addAll(user1ConversationList);
        } else if (user1ConversationList == null
                && user2ConversationList != null) {
            combineConversationList.addAll(user2ConversationList);
        } else {
            combineConversationList.addAll(user1ConversationList);
            combineConversationList.addAll(user2ConversationList);
        }

        List<ShowMessageModel> messageModelList = new ArrayList<>();

        for (Conversation conversation : combineConversationList) {
            Page<Message> pagingResult =
                    messageRepository
                            .findAllByConversationId(conversation.getConversationId(), paging);

            messageModelList.addAll(fillAllMessageModel(pagingResult));
        }

        return messageModelList;
    }

    @Override
    public List<ShowMessageModel> getByConversationIdUnauthenticated(
            String ipAddress, Long conversationId, Pageable paging) throws Exception {
        List<Conversation> conversationList =
                conversationRepository.findAllByIpAddress(ipAddress);

        if (conversationList != null) {
            List<ShowMessageModel> result = new ArrayList<>();

            for (Conversation conversation : conversationList) {
                Page<Message> pagingResult =
                        messageRepository
                                .findAllByConversationId(conversation.getConversationId(), paging);

                result.addAll(fillAllMessageModel(pagingResult));
            }

            return result;
        }
        return null;
    }

    @Override
    public Long sendMessageAuthenticated(Long userId, Long conversationId, String message) {
        Message messageEntity = new Message();
        messageEntity.setConversationId(conversationId);
        messageEntity.setSenderId(userId);
        messageEntity.setMessage(message);
        messageEntity.setSendTime(LocalDateTime.now());

        Message createdMessage = messageRepository.saveAndFlush(messageEntity);

        if (createdMessage == null)
            return null;

        return createdMessage.getMessageId();
    }

    @Override
    public Long sendMessageUnauthenticated(String ipAddress, Long conversationId, String message) {
        Message messageEntity = new Message();
        messageEntity.setConversationId(conversationId);
        messageEntity.setSenderIp(ipAddress);
        messageEntity.setMessage(message);
        messageEntity.setSendTime(LocalDateTime.now());
        Message createdMessage = messageRepository.saveAndFlush(messageEntity);

        if (createdMessage == null)
            return null;

        return createdMessage.getMessageId();
    }

    @Override
    public boolean seenMessageAuthenticated(Long userId, Long conversationId) {
        List<Message> listMessage = messageRepository.findAllByConversationIdAndSenderId(conversationId, userId);
        if (listMessage != null) {
            for (Message message : listMessage) {
                message.setSeen(true);
                messageRepository.saveAndFlush(message);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean seenMessageUnauthenticated(String ipAddress, Long conversationId) {
        List<Message> listMessage = messageRepository.findAllByConversationIdAndSenderIp(conversationId, ipAddress);
        if (listMessage != null) {
            for (Message message : listMessage) {
                message.setSeen(true);
                messageRepository.saveAndFlush(message);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean existsById(Long messageId) {
        return messageRepository
                .existsByMessageId(messageId);
    }

    private List<ShowMessageModel> fillAllMessageModel(Page<Message> pagingResult) throws Exception {
        if (pagingResult.hasContent()) {
            int totalPage = pagingResult.getTotalPages();

            Set<Long> messageIdSet = pagingResult.stream()
                    .map(Message::getMessageId)
                    .collect(Collectors.toSet());

            // get file
            Map<Long, List<ExternalFileReadDTO>> messageIdFileDTOListMap =
                    eFEWPairingService.mapEntityIdExternalFileDTOListByEntityIdInAndEntityType(
                            messageIdSet, ENTITY_TYPE);

            Page<ShowMessageModel> modelResult = pagingResult.map(new Converter<Message, ShowMessageModel>() {
                @Override
                protected ShowMessageModel doForward(Message message) {
                    ShowMessageModel model = new ShowMessageModel();

                    long messageId = message.getMessageId();

                    model.setMessageId(messageId);
                    model.setConversationId(message.getConversationId());
                    model.setSenderId(message.getSenderId());
                    model.setSenderIp(message.getSenderIp());
                    model.setMessage(message.getMessage());
                    model.setSeen(message.getSeen());
                    model.setSendTime(message.getSendTime());

                    model.setFileList(
                            messageIdFileDTOListMap.get(messageId));

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
