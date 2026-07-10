package com.recycling.service;

import com.recycling.model.User;
import com.recycling.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des utilisateurs
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ActivityLogService activityLogService;

    /**
     * Créer un nouvel utilisateur
     */
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsActive(true);
        user.setLoginAttempts(0);
        User savedUser = userRepository.save(user);
        
        // Log l'action
        activityLogService.logActivity(null, "SYSTEM", 
            ActivityLogService.ActionType.CREATE, "User", savedUser.getId(), 
            "Création de l'utilisateur: " + savedUser.getUsername(), null, null);
        
        return savedUser;
    }

    /**
     * Récupérer un utilisateur par ID
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Récupérer un utilisateur par nom d'utilisateur
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Récupérer un utilisateur par email
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Mettre à jour les informations d'un utilisateur
     */
    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId())
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setRole(user.getRole());
        
        User updatedUser = userRepository.save(existingUser);
        
        activityLogService.logActivity(user.getId(), user.getUsername(), 
            ActivityLogService.ActionType.UPDATE, "User", user.getId(), 
            "Mise à jour des informations utilisateur", null, null);
        
        return updatedUser;
    }

    /**
     * Changer le mot de passe d'un utilisateur
     */
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Ancien mot de passe incorrect");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordChangedAt(LocalDateTime.now());
        userRepository.save(user);
        
        activityLogService.logActivity(userId, user.getUsername(), 
            ActivityLogService.ActionType.PASSWORD_CHANGE, "User", userId, 
            "Changement de mot de passe", null, null);
    }

    /**
     * Réinitialiser le mot de passe (administrateur)
     */
    public String resetPassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordChangedAt(LocalDateTime.now());
        userRepository.save(user);
        
        activityLogService.logActivity(null, "ADMIN", 
            ActivityLogService.ActionType.PASSWORD_CHANGE, "User", userId, 
            "Réinitialisation du mot de passe pour: " + user.getUsername(), null, null);
        
        return newPassword;
    }

    /**
     * Désactiver un utilisateur
     */
    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        user.setIsActive(false);
        userRepository.save(user);
        
        activityLogService.logActivity(null, "ADMIN", 
            ActivityLogService.ActionType.UPDATE, "User", userId, 
            "Désactivation de l'utilisateur: " + user.getUsername(), null, null);
    }

    /**
     * Activer un utilisateur
     */
    public void activateUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        user.setIsActive(true);
        userRepository.save(user);
        
        activityLogService.logActivity(null, "ADMIN", 
            ActivityLogService.ActionType.UPDATE, "User", userId, 
            "Activation de l'utilisateur: " + user.getUsername(), null, null);
    }

    /**
     * Supprimer un utilisateur
     */
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        userRepository.delete(user);
        
        activityLogService.logActivity(null, "ADMIN", 
            ActivityLogService.ActionType.DELETE, "User", userId, 
            "Suppression de l'utilisateur: " + user.getUsername(), null, null);
    }

    /**
     * Lister tous les utilisateurs actifs
     */
    public List<User> getAllActiveUsers() {
        return userRepository.findByIsActive(true);
    }

    /**
     * Lister tous les utilisateurs
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Rechercher des utilisateurs
     */
    public List<User> searchUsers(String search) {
        return userRepository.searchUsers(search);
    }

    /**
     * Obtenir le nombre d'utilisateurs actifs
     */
    public long getActiveUsersCount() {
        return userRepository.countActiveUsers();
    }

    /**
     * Verrouiller un compte utilisateur
     */
    public void lockUserAccount(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        user.setIsLocked(true);
        userRepository.save(user);
    }

    /**
     * Déverrouiller un compte utilisateur
     */
    public void unlockUserAccount(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        user.setIsLocked(false);
        user.setLoginAttempts(0);
        userRepository.save(user);
    }
}