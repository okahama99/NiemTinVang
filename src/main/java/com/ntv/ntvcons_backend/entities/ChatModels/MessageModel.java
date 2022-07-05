package com.ntv.ntvcons_backend.entities.ChatModels;

import com.ntv.ntvcons_backend.Enum.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MessageModel {
    private String userName, message, avatar;
    private Date date;
    private double totalPage;
    private Long userId, messageId;
    private Status status;
}
