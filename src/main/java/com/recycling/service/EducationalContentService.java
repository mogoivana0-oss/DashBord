package com.recycling.service;

import com.recycling.model.EducationalContent;
import com.recycling.repository.EducationalContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion du contenu éducatif
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EducationalContentService {

    private final EducationalContentRepository contentRepository;
    private final ActivityLogService activityLogService;

    /**
     * Créer un nouveau contenu
     */
    public EducationalContent createContent(EducationalContent content) {
        EducationalContent savedContent = contentRepository.save(content);
        
        activityLogService.logActivity(null, "SYSTEM", 
            ActivityLogService.ActionType.CREATE, "EducationalContent", savedContent.getId(), 
            "Création du contenu éducatif: " + savedContent.getTitle(), null, null);
        
        return savedContent;
    }

    /**
     * Récupérer un contenu par ID
     */
    public Optional<EducationalContent> getContentById(Long id) {
        Optional<EducationalContent> content = contentRepository.findById(id);
        // Incrémenter le compteur de vues
        content.ifPresent(c -> {
            c.setViewCount((c.getViewCount() != null ? c.getViewCount() : 0L) + 1);
            contentRepository.save(c);
        });
        return content;
    }

    /**
     * Mettre à jour un contenu
     */
    public EducationalContent updateContent(EducationalContent content) {
        EducationalContent existingContent = contentRepository.findById(content.getId())
            .orElseThrow(() -> new RuntimeException("Contenu non trouvé"));
        
        existingContent.setTitle(content.getTitle());
        existingContent.setContent(content.getContent());
        existingContent.setContentType(content.getContentType());
        existingContent.setCategory(content.getCategory());
        existingContent.setThumbnailUrl(content.getThumbnailUrl());
        existingContent.setMediaUrl(content.getMediaUrl());
        existingContent.setTags(content.getTags());
        existingContent.setDurationMinutes(content.getDurationMinutes());
        
        EducationalContent updatedContent = contentRepository.save(existingContent);
        
        activityLogService.logActivity(null, "ADMIN", 
            ActivityLogService.ActionType.UPDATE, "EducationalContent", content.getId(), 
            "Mise à jour du contenu: " + content.getTitle(), null, null);
        
        return updatedContent;
    }

    /**
     * Publier un contenu
     */
    public EducationalContent publishContent(Long contentId) {
        EducationalContent content = contentRepository.findById(contentId)
            .orElseThrow(() -> new RuntimeException("Contenu non trouvé"));
        
        content.setIsPublished(true);
        content.setPublishedAt(LocalDateTime.now());
        
        EducationalContent publishedContent = contentRepository.save(content);
        
        activityLogService.logActivity(null, "ADMIN", 
            ActivityLogService.ActionType.UPDATE, "EducationalContent", contentId, 
            "Publication du contenu: " + content.getTitle(), null, null);
        
        return publishedContent;
    }

    /**
     * Dépublier un contenu
     */
    public EducationalContent unpublishContent(Long contentId) {
        EducationalContent content = contentRepository.findById(contentId)
            .orElseThrow(() -> new RuntimeException("Contenu non trouvé"));
        
        content.setIsPublished(false);
        
        EducationalContent unpublishedContent = contentRepository.save(content);
        
        activityLogService.logActivity(null, "ADMIN", 
            ActivityLogService.ActionType.UPDATE, "EducationalContent", contentId, 
            "Dépublication du contenu: " + content.getTitle(), null, null);
        
        return unpublishedContent;
    }

    /**
     * Supprimer un contenu
     */
    public void deleteContent(Long contentId) {
        EducationalContent content = contentRepository.findById(contentId)
            .orElseThrow(() -> new RuntimeException("Contenu non trouvé"));
        
        contentRepository.delete(content);
        
        activityLogService.logActivity(null, "ADMIN", 
            ActivityLogService.ActionType.DELETE, "EducationalContent", contentId, 
            "Suppression du contenu: " + content.getTitle(), null, null);
    }

    /**
     * Récupérer tous les contenus publiés
     */
    public List<EducationalContent> getAllPublishedContent() {
        return contentRepository.findAllPublishedOrderByDate();
    }

    /**
     * Récupérer tous les contenus
     */
    public List<EducationalContent> getAllContent() {
        return contentRepository.findAll();
    }

    /**
     * Récupérer les contenus par type
     */
    public List<EducationalContent> getContentByType(EducationalContent.ContentType contentType) {
        return contentRepository.findByContentType(contentType);
    }

    /**
     * Récupérer les contenus par catégorie
     */
    public List<EducationalContent> getContentByCategory(String category) {
        return contentRepository.findByCategory(category);
    }

    /**
     * Rechercher des contenus
     */
    public List<EducationalContent> searchContent(String search) {
        return contentRepository.searchContent(search);
    }

    /**
     * Récupérer les contenus les plus consultés
     */
    public List<EducationalContent> getMostViewedContent() {
        return contentRepository.findMostViewedContent();
    }
}