package com.ntv.ntvcons_backend.entities.ConversationModels;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ShowConversationModel {
    private String name;
    private String avatar;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
}
