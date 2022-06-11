package com.ntv.ntvcons_backend.dtos.post;

import com.ntv.ntvcons_backend.dtos.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO implements Serializable {
    private Integer postId;
    private UserDTO author;
    private String postTitle;
    private Instant createDate;
    private Boolean isDeleted = false;
}
