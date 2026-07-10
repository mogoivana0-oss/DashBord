package com.recycling.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entité pour l'historique des interventions sur un signalement
 */
@Entity
@Table(name = "report_interventions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportIntervention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @Column(name = "team_member_id")
    private Long teamMemberId;

    @Column(name = "team_member_name")
    private String teamMemberName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "action_type")
    private String actionType;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "photo_url")
    private String photoUrl;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
