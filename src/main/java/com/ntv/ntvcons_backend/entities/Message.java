package com.ntv.ntvcons_backend.entities;

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
@Table(name = "message")
public class Message /*extends BaseEntity*/{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "messageId", nullable = false)
    private Long messageId;

    @Column(name = "conversationId", nullable = false)
    private Long conversationId;

    @Column(name = "senderId")
    private Long senderId;

    /* IPv6 max 61 chars */
    @Column(name = "senderIp", length = 61)
    private String senderIp;

    @Column(name = "sendTime", nullable = false)
    private LocalDateTime sendTime;

    @Column(name = "message")
    private String message;

    @Column(name = "seen", nullable = false)
    private Boolean seen = false;

//    @Column(name = "fileName", length = 100)
//    private String fileName;
//
//    @Column(name = "fileType", length = 200)
//    private String fileType;
//
//    @Column(name = "data", length = 1024)
//    private byte[] data;

}
