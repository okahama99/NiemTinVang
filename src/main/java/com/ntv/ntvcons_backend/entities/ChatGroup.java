package com.ntv.ntvcons_backend.entities;

import com.ntv.ntvcons_backend.constants.Status;
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
@Table(name = "chat_group")
public class ChatGroup extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatGroupId", nullable = false)
    private Long chatGroupId;

    @Column(name = "projectId", nullable = false)
    private Long projectId;

    @Column(name = "groupName", nullable = false)
    private String groupName;

    @Column(name = "isInternalGroup", nullable = false)
    private Boolean isInternalGroup = false;
}
