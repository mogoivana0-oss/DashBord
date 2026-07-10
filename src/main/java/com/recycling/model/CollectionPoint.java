package com.recycling.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entité Point de Collecte avec gestion de localisation et capacité
 */
@Entity
@Table(name = "collection_points")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollectionPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "operating_hours")
    private String operatingHours;

    @Column(name = "capacity_kg")
    private Integer capacityKg;

    @Column(name = "current_usage_kg")
    private Integer currentUsageKg = 0;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "collection_point_waste",
            joinColumns = @JoinColumn(name = "collection_point_id"),
            inverseJoinColumns = @JoinColumn(name = "waste_id")
    )
    private Set<Waste> acceptedWaste = new HashSet<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "has_parking")
    private boolean hasParking = false;

    @Column(name = "is_wheelchair_accessible")
    private boolean isWheelchairAccessible = false;

    @Column(name = "image_url")
    private String imageUrl;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public double getCapacityPercentage() {
        if (capacityKg == null || capacityKg == 0) {
            return 0;
        }
        return (currentUsageKg != null ? currentUsageKg : 0) * 100.0 / capacityKg;
    }
}
