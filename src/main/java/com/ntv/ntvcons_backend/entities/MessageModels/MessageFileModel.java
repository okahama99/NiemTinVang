package com.ntv.ntvcons_backend.entities.MessageModels;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageFileModel {

    private String fileName, fileLink, fileType;
    private LocalDateTime messageCreatedAt;
}
