package com.ntv.ntvcons_backend.entities.UserModels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFCMToken {
    private String fcmToken;
    private Long userid;
    private String avatar;
    private String userName;
    private String email;
    private String phone;
}
