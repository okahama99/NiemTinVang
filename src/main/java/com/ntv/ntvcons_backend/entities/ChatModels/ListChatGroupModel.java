package com.ntv.ntvcons_backend.entities.ChatModels;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ListChatGroupModel {
    private String newestMessage, userName;
    private double totalPage;
    private Long userId;
}
