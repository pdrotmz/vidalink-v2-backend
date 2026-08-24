package com.vidalink.healthcare.gamification.entity.domain.pointtransaction;

import com.vidalink.healthcare.gamification.entity.enums.pointtransaction.PointTransactionSource;
import com.vidalink.healthcare.gamification.entity.enums.pointtransaction.PointTransactionType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "point_transaction")
@Table(name = "tbl_point_transaction")
@Data
public class PointTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "point_transaction_type", nullable = false)
    private PointTransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "point_transaction_source", nullable = false)
    private PointTransactionSource source;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
