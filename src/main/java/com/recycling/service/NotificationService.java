package com.recycling.service;

import com.recycling.model.Notification;
import com.recycling.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service pour la gestion des notifications
 */
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * Créer et envoyer une notification
     */
    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    /**
     * Envoyer une notification à un utilisateur
     */
    public Notification sendNotificationToUser(Long userId, String title, String message, 
                                                Notification.NotificationType type) {
        Notification notification = Notification.builder()
            .userId(userId)
            .title(title)
            .message(message)
            .type(type)
            .isRead(false)
            .build();
        
        return notificationRepository.save(notification);
    }

    /**
     * Envoyer une notification à tous les utilisateurs
     */
    public void broadcastNotification(String title, String message, 
                                      Notification.NotificationType type, 
                                      List<Long> userIds) {
        for (Long userId : userIds) {
            sendNotificationToUser(userId, title, message, type);
        }
    }

    /**
     * Récupérer les notifications d'un utilisateur
     */
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findUserNotificationsOrderByDate(userId);
    }

    /**
     * Récupérer les notifications non lues d'un utilisateur
     */
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsRead(userId, false);
    }

    /**
     * Marquer une notification comme lue
     */
    public Notification markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new RuntimeException("Notification non trouvée"));
        
        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());
        
        return notificationRepository.save(notification);
    }

    /**
     * Marquer toutes les notifications comme lues
     */
    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = getUnreadNotifications(userId);
        for (Notification notification : unreadNotifications) {
            markAsRead(notification.getId());
        }
    }

    /**
     * Compter les notifications non lues
     */
    public long countUnreadNotifications(Long userId) {
        return notificationRepository.countUnreadNotifications(userId);
    }

    /**
     * Supprimer une notification
     */
    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    /**
     * Supprimer toutes les notifications lues d'un utilisateur
     */
    public void deleteReadNotifications(Long userId) {
        List<Notification> readNotifications = notificationRepository.findByUserIdAndIsRead(userId, true);
        notificationRepository.deleteAll(readNotifications);
    }
}