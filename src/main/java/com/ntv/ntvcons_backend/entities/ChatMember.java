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
@Table(name = "chat_member")
public class ChatMember extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatMemberId", nullable = false)
    private Long chatMemberId;

    @Column(name = "chatGroupId", nullable = false)
    private Long chatGroupId;

    @Column(name = "memberId", nullable = false)
    private Long memberId;
}
