package com.ntv.ntvcons_backend.entities;

import com.ntv.ntvcons_backend.Enum.Status;
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
@Table(name = "notification")
public class Notification extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notificationId", nullable = false)
    private Long notificationId;

    @Column(name = "notificationTriggerId", nullable = false)
    private Long notificationTriggerId;

    @Column(name = "notificationTitle", nullable = false)
    private String notificationTitle;

    @Column(name = "notificationDesc", nullable = false)
    private String notificationDesc;

    @Column(name = "status", nullable = false)
    private Status status;
}
