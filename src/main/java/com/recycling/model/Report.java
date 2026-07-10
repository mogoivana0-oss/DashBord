package com.recycling.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité Signalement pour la gestion des reclamations et problèmes
 */
@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status = ReportStatus.NEW;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportCategory category;

    @Column(name = "citizen_name")
    private String citizenName;

    @Column(name = "citizen_email")
    private String citizenEmail;

    @Column(name = "citizen_phone")
    private String citizenPhone;

    @Column(name = "location_address")
    private String locationAddress;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "assigned_to_user_id")
    private Long assignedToUserId;

    @Column(name = "assigned_team")
    private String assignedTeam;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(columnDefinition = "TEXT", name = "resolution_notes")
    private String resolutionNotes;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReportIntervention> interventions = new ArrayList<>();

    @Column(name = "priority")
    private String priority = "NORMAL"; // LOW, NORMAL, HIGH, URGENT

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum ReportStatus {
        NEW("Nouveau"),
        IN_PROGRESS("En cours"),
        ON_HOLD("En attente"),
        RESOLVED("Résolu"),
        CLOSED("Fermé"),
        REJECTED("Rejeté");

        private final String displayName;

        ReportStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum ReportCategory {
        OVERFLOWING_BIN("Bac débordant"),
        DIRTY_LOCATION("Lieu sale"),
        MISSING_BIN("Bac manquant"),
        BROKEN_BIN("Bac cassé"),
        ILLEGAL_DUMPING("Dépôt sauvage"),
        ODOR_ISSUE("Problème d'odeur"),
        OTHER("Autre");

        private final String displayName;

        ReportCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
