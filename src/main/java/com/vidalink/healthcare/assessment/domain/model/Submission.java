package com.vidalink.healthcare.assessment.domain.model;

import com.vidalink.healthcare.assessment.domain.enums.ValidationStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "submission")
@Table(name = "tbl_submission")
@Data
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "id_user", nullable = false)
    private UUID idUser;

    @Column(name = "sent_time", updatable = false, nullable = false)
    private LocalDateTime sentTime;

    @Column(name = "file")
    private String file;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ValidationStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
