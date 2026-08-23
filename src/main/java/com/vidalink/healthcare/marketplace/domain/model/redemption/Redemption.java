package com.vidalink.healthcare.marketplace.domain.model.redemption;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "redemption")
@Table(name = "tbl_redemption")
@Data
public class Redemption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "id_user", nullable = false, updatable = false)
    private UUID idUser;

    @Column(name = "id_reward", nullable = false, updatable = false)
    private UUID idReward;

    @Column(name = "amount", nullable = false, updatable = false)
    private int amount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
