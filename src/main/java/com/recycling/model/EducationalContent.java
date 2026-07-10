package com.recycling.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entité Contenu Éducatif pour les articles et vidéos
 */
@Entity
@Table(name = "educational_content")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducationalContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContentType contentType;

    @Column(name = "category")
    private String category;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "media_url")
    private String mediaUrl;

    @Column(name = "author")
    private String author;

    @Column(name = "is_published")
    private boolean isPublished = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "view_count")
    private Long viewCount = 0L;

    @Column(columnDefinition = "TEXT", name = "tags")
    private String tags;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum ContentType {
        ARTICLE("Article"),
        VIDEO("Vidéo"),
        INFOGRAPHIC("Infographie"),
        TUTORIAL("Tutoriel"),
        GUIDE("Guide");

        private final String displayName;

        ContentType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
