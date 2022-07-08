package com.ntv.ntvcons_backend.dtos.post;

import com.ntv.ntvcons_backend.dtos.BaseReadDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostReadDTO extends BaseReadDTO {
    private Long postId;
    private String authorName;
    private String postTitle;
}
