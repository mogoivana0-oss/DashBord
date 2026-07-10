package com.recycling.repository;

import com.recycling.model.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findByUserId(Long userId);
    List<ActivityLog> findByActionType(ActivityLog.ActionType actionType);
    List<ActivityLog> findByEntityType(String entityType);
    
    @Query("SELECT al FROM ActivityLog al WHERE al.createdAt BETWEEN :startDate AND :endDate ORDER BY al.createdAt DESC")
    List<ActivityLog> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                       @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT al FROM ActivityLog al WHERE al.userId = :userId ORDER BY al.createdAt DESC")
    List<ActivityLog> findUserActivityOrderByDate(@Param("userId") Long userId);
}
