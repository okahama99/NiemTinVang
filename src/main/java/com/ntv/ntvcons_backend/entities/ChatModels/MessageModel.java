package com.ntv.ntvcons_backend.entities.ChatModels;

import com.ntv.ntvcons_backend.Enum.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageModel {
    private String userName, message;
    private LocalDateTime date;
    private double totalPage;
    private Long userId, messageId;
    private Status status;
}
