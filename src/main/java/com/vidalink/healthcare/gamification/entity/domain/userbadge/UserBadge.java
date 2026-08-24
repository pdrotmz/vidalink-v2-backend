package com.vidalink.healthcare.gamification.entity.domain.userbadge;

import com.vidalink.healthcare.gamification.entity.enums.badge.Badge;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "user_badge")
@Table(name = "tbl_user_badge", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "badge"})})
@Data
public class UserBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "badge", nullable = false)
    private Badge badge;

    @CreationTimestamp
    private LocalDateTime earnedAt;
}
