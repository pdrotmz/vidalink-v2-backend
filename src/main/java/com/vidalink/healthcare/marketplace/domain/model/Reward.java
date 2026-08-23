package com.vidalink.healthcare.marketplace.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity(name = "reward")
@Table(name = "tbl_reward")
@Data
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, updatable = true, length = 100)
    private String name;

    @Column(name = "description", nullable = false, updatable = true, columnDefinition = "TEXT")
    private String description;

    @Column(name = "stock", nullable = false, updatable = true)
    private int stock;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "is_active", nullable = false, updatable = true, columnDefinition = "boolean default true")
    private boolean isActive;
}
