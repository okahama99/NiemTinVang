package com.ntv.ntvcons_backend.entities.MessageModels;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ShowMessageModel{
    private Long messageId, conversationId, senderId;
    private String senderIp, message, fileName, fileType;
    private Boolean seen;
    private byte[] data;
    private LocalDateTime sendTime;

    private Long createdBy, updatedBy;
    private LocalDateTime createdAt, updatedAt;
    private Integer totalPage;
}
