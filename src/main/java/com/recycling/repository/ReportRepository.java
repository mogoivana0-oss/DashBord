package com.recycling.repository;

import com.recycling.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByStatus(Report.ReportStatus status);
    List<Report> findByCategory(Report.ReportCategory category);
    List<Report> findByPriority(String priority);
    
    @Query("SELECT r FROM Report r WHERE r.status = :status ORDER BY r.createdAt DESC")
    List<Report> findByStatusOrderByCreatedAtDesc(@Param("status") Report.ReportStatus status);
    
    @Query("SELECT COUNT(r) FROM Report r WHERE r.status = 'NEW'")
    long countNewReports();
    
    @Query("SELECT COUNT(r) FROM Report r WHERE r.status = 'RESOLVED'")
    long countResolvedReports();
    
    @Query("SELECT r FROM Report r WHERE r.title LIKE %:search% OR r.description LIKE %:search%")
    List<Report> searchReports(@Param("search") String search);
    
    @Query("SELECT r FROM Report r WHERE r.createdAt BETWEEN :startDate AND :endDate")
    List<Report> findReportsByDateRange(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);
}
