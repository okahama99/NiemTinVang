package com.ntv.ntvcons_backend.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Note {
    private String function;
    private Long userId,notiId;
    private String content;
    private Map<String, String> data;
    private String image;
}
