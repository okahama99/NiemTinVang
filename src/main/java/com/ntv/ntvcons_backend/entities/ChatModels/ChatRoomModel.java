package com.ntv.ntvcons_backend.entities.ChatModels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatRoomModel {
    private String chatRoomId;
    private Long userId1, userId2;

    public ChatRoomModel() {

    }
}
