package com.ntv.ntvcons_backend.services.message;

import com.google.common.base.Converter;
import com.ntv.ntvcons_backend.constants.EntityType;
import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileReadDTO;
import com.ntv.ntvcons_backend.dtos.user.UserReadDTO;
import com.ntv.ntvcons_backend.entities.Message;
import com.ntv.ntvcons_backend.entities.MessageModels.ShowMessageModel;
import com.ntv.ntvcons_backend.repositories.MessageRepository;
import com.ntv.ntvcons_backend.services.externalFileEntityWrapperPairing.ExternalFileEntityWrapperPairingService;
import com.ntv.ntvcons_backend.services.user.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ExternalFileEntityWrapperPairingService eFEWPairingService;
    @Autowired
    private UserService userService;

    private final EntityType ENTITY_TYPE = EntityType.MESSAGE_ENTITY;

    @Override
    public List<ShowMessageModel> getByConversationId(Long conversationId, Pageable paging) throws Exception {

        List<ShowMessageModel> messageModelList = new ArrayList<>();

        Page<Message> pagingResult =
                messageRepository
                        .findAllByConversationId(conversationId, paging);

        messageModelList.addAll(fillAllMessageModel(pagingResult));

        return messageModelList;
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
                @SneakyThrows
                @Override
                protected ShowMessageModel doForward(Message message) {
                    ShowMessageModel model = new ShowMessageModel();

                    long messageId = message.getMessageId();

                    // get avatar
                    UserReadDTO userDTO = userService.getDTOById(message.getSenderId());
                    String avatarLink = "Không có avatar";
                    if (userDTO.getFile() != null){
                        avatarLink = userDTO.getFile().getFileLink();
                    }
                    model.setAvatarLink(avatarLink);

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
