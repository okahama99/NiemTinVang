package com.ntv.ntvcons_backend.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "conversation")
public class Conversation extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversationId", nullable = false)
    private Long conversationId;

    @Column(name = "user1Id")
    private Long user1Id;

    @Column(name = "user2Id")
    private Long user2Id;

    @Column(name = "ipAddress", length = 61)
    private String ipAddress;

    @Column(name = "clientName")
    private String clientName;
}
