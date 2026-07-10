package com.recycling.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entité Déchet avec consignes de tri et informations de valorisation
 */
@Entity
@Table(name = "waste")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Waste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "category")
    private String category;

    @Column(columnDefinition = "TEXT", name = "sorting_instructions")
    private String sortingInstructions;

    @Column(name = "bin_color")
    private String binColor; // Ex: "Jaune", "Bleu", "Vert", "Gris"

    @Column(name = "bin_image")
    private String binImage;

    @Column(name = "waste_image")
    private String wasteImage;

    @Column(name = "is_recyclable")
    private boolean isRecyclable;

    @Column(name = "is_hazardous")
    private boolean isHazardous;

    @Column(columnDefinition = "TEXT", name = "reuse_tips")
    private String reuseTips;

    @Column(name = "recycling_process", columnDefinition = "TEXT")
    private String recyclingProcess;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "search_count")
    private Long searchCount = 0L;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
