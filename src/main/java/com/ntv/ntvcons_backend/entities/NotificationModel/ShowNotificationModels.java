package com.ntv.ntvcons_backend.entities.NotificationModel;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ShowNotificationModels {

    private Long id;

    private Date createdAt;

    private String description;

    private String userName;

    private String chatRoomName;

    //TODO : thêm field id các table nếu có nối thêm

    private double totalPage;

}
