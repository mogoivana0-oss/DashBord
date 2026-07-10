package com.recycling.repository;

import com.recycling.model.Waste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WasteRepository extends JpaRepository<Waste, Long> {
    Optional<Waste> findByName(String name);
    List<Waste> findByCategory(String category);
    List<Waste> findByIsActive(boolean isActive);
    List<Waste> findByIsRecyclable(boolean isRecyclable);
    List<Waste> findByIsHazardous(boolean isHazardous);
    
    @Query("SELECT w FROM Waste w WHERE w.isActive = true ORDER BY w.searchCount DESC")
    List<Waste> findMostSearchedWaste();
    
    @Query("SELECT w FROM Waste w WHERE w.name LIKE %:search% OR w.description LIKE %:search%")
    List<Waste> searchWaste(@Param("search") String search);
}
