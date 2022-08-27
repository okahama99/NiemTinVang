package com.ntv.ntvcons_backend.entities.MessageModels;

import com.ntv.ntvcons_backend.dtos.externalFile.ExternalFileReadDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ShowMessageModel{
    private Long messageId, conversationId, senderId;
    private String senderIp, message;
    private Boolean seen;
    private LocalDateTime sendTime;

    private List<ExternalFileReadDTO> fileList;

    private Long createdBy, updatedBy;
    private LocalDateTime createdAt, updatedAt;
    private Integer totalPage;
}
