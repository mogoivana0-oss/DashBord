package com.recycling.repository;

import com.recycling.model.EducationalContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationalContentRepository extends JpaRepository<EducationalContent, Long> {
    List<EducationalContent> findByContentType(EducationalContent.ContentType contentType);
    List<EducationalContent> findByCategory(String category);
    List<EducationalContent> findByIsPublished(boolean isPublished);
    
    @Query("SELECT ec FROM EducationalContent ec WHERE ec.isPublished = true ORDER BY ec.publishedAt DESC")
    List<EducationalContent> findAllPublishedOrderByDate();
    
    @Query("SELECT ec FROM EducationalContent ec WHERE ec.title LIKE %:search% OR ec.tags LIKE %:search%")
    List<EducationalContent> searchContent(@Param("search") String search);
    
    @Query("SELECT ec FROM EducationalContent ec WHERE ec.isPublished = true ORDER BY ec.viewCount DESC")
    List<EducationalContent> findMostViewedContent();
}
