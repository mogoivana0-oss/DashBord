package com.recycling.service;

import com.recycling.model.ActivityLog;
import com.recycling.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service pour la gestion des logs d'activité
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    public enum ActionType {
        LOGIN, LOGOUT, CREATE, UPDATE, DELETE, VIEW, DOWNLOAD, UPLOAD, 
        PASSWORD_CHANGE, PERMISSION_CHANGE, BACKUP, RESTORE, SYSTEM_CONFIG, OTHER
    }

    /**
     * Enregistrer une activité
     */
    public ActivityLog logActivity(Long userId, String username, ActionType actionType, 
                                    String entityType, Long entityId, String details, 
                                    String ipAddress, String userAgent) {
        ActivityLog log = ActivityLog.builder()
            .userId(userId)
            .username(username)
            .actionType(ActivityLog.ActionType.valueOf(actionType.name()))
            .entityType(entityType)
            .entityId(entityId)
            .details(details)
            .ipAddress(ipAddress)
            .userAgent(userAgent)
            .build();
        
        return activityLogRepository.save(log);
    }

    /**
     * Récupérer l'historique d'un utilisateur
     */
    public List<ActivityLog> getUserActivityHistory(Long userId) {
        return activityLogRepository.findUserActivityOrderByDate(userId);
    }

    /**
     * Récupérer les activités par type
     */
    public List<ActivityLog> getActivitiesByType(ActionType actionType) {
        return activityLogRepository.findByActionType(
            ActivityLog.ActionType.valueOf(actionType.name()));
    }

    /**
     * Récupérer les activités sur une plage de dates
     */
    public List<ActivityLog> getActivitiesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return activityLogRepository.findByDateRange(startDate, endDate);
    }

    /**
     * Récupérer tous les logs
     */
    public List<ActivityLog> getAllLogs() {
        return activityLogRepository.findAll();
    }
}