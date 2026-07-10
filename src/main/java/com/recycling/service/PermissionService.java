package com.recycling.service;

import com.recycling.model.Permission;
import com.recycling.model.User;
import com.recycling.repository.PermissionRepository;
import com.recycling.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service pour la gestion des permissions et des autorisations
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final ActivityLogService activityLogService;

    /**
     * Créer une nouvelle permission
     */
    public Permission createPermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    /**
     * Récupérer une permission par ID
     */
    public Optional<Permission> getPermissionById(Long id) {
        return permissionRepository.findById(id);
    }

    /**
     * Récupérer une permission par nom
     */
    public Optional<Permission> getPermissionByName(String name) {
        return permissionRepository.findByName(name);
    }

    /**
     * Lister toutes les permissions
     */
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    /**
     * Ajouter une permission à un utilisateur
     */
    public void grantPermissionToUser(Long userId, Long permissionId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Permission permission = permissionRepository.findById(permissionId)
            .orElseThrow(() -> new RuntimeException("Permission non trouvée"));
        
        user.getPermissions().add(permission);
        userRepository.save(user);
        
        activityLogService.logActivity(null, "ADMIN", 
            ActivityLogService.ActionType.PERMISSION_CHANGE, "User", userId, 
            "Permission accordée: " + permission.getName(), null, null);
    }

    /**
     * Retirer une permission d'un utilisateur
     */
    public void revokePermissionFromUser(Long userId, Long permissionId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        user.getPermissions().removeIf(p -> p.getId().equals(permissionId));
        userRepository.save(user);
        
        activityLogService.logActivity(null, "ADMIN", 
            ActivityLogService.ActionType.PERMISSION_CHANGE, "User", userId, 
            "Permission révoquée: " + permissionId, null, null);
    }

    /**
     * Récupérer les permissions d'un utilisateur
     */
    public Set<Permission> getUserPermissions(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return user.getPermissions();
    }

    /**
     * Vérifier si un utilisateur a une permission spécifique
     */
    public boolean userHasPermission(Long userId, String permissionName) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        return user.getPermissions().stream()
            .anyMatch(p -> p.getName().equals(permissionName));
    }

    /**
     * Ajouter plusieurs permissions à un utilisateur
     */
    public void grantPermissionsToUser(Long userId, List<Long> permissionIds) {
        for (Long permissionId : permissionIds) {
            grantPermissionToUser(userId, permissionId);
        }
    }

    /**
     * Réinitialiser les permissions d'un utilisateur
     */
    public void resetUserPermissions(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        user.getPermissions().clear();
        userRepository.save(user);
        
        activityLogService.logActivity(null, "ADMIN", 
            ActivityLogService.ActionType.PERMISSION_CHANGE, "User", userId, 
            "Réinitialisation des permissions utilisateur", null, null);
    }
}