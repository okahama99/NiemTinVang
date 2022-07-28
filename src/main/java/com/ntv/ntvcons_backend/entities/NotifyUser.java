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
@Table(name = "notify_user")
public class NotifyUser extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notifyId", nullable = false)
    private Long notifyId;

    @Column(name = "notificationId", nullable = false)
    private Long notificationId;

    @Column(name = "receiverId", nullable = false)
    private Long receiverId;

    @Column(name = "status", nullable = false)
    private Status status;
}
