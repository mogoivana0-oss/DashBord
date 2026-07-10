package com.recycling.repository;

import com.recycling.model.CollectionPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionPointRepository extends JpaRepository<CollectionPoint, Long> {
    List<CollectionPoint> findByIsActive(boolean isActive);
    List<CollectionPoint> findByCity(String city);
    
    @Query("SELECT cp FROM CollectionPoint cp WHERE cp.isActive = true ORDER BY cp.name")
    List<CollectionPoint> findAllActive();
    
    @Query("SELECT COUNT(cp) FROM CollectionPoint cp WHERE cp.isActive = true")
    long countActiveCollectionPoints();
    
    @Query("SELECT cp FROM CollectionPoint cp WHERE cp.name LIKE %:search% OR cp.city LIKE %:search%")
    List<CollectionPoint> searchCollectionPoints(@Param("search") String search);
    
    @Query("SELECT cp FROM CollectionPoint cp WHERE cp.currentUsageKg > (cp.capacityKg * 0.8)")
    List<CollectionPoint> findNearlyFullCollectionPoints();
}
