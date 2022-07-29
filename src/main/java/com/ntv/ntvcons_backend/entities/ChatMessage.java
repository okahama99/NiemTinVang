package com.ntv.ntvcons_backend.entities;

import com.ntv.ntvcons_backend.constants.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "chat_message")
public class ChatMessage extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "messageId", nullable = false)
    private Long messageId;

    @Column(name = "chatGroupId", nullable = false)
    private Long chatGroupId;

    @Column(name = "posterId", nullable = false)
    private Long posterId;

    @Column(name = "postTime", nullable = false)
    private LocalDateTime postTime;

    @Column(name = "message", nullable = false)
    private String message;
}
