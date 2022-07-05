package com.ntv.ntvcons_backend.entities.ChatModels;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ListChatRoomModel {
    private String newestMessage, userName, avatar;
    private double totalPage;
    private Long userId;
}
