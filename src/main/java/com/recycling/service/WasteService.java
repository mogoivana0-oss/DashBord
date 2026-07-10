package com.recycling.service;

import com.recycling.model.Waste;
import com.recycling.repository.WasteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des déchets
 */
@Service
@RequiredArgsConstructor
@Transactional
public class WasteService {

    private final WasteRepository wasteRepository;
    private final ActivityLogService activityLogService;

    /**
     * Créer un nouveau déchet
     */
    public Waste createWaste(Waste waste) {
        Waste savedWaste = wasteRepository.save(waste);
        
        activityLogService.logActivity(null, "SYSTEM", 
            ActivityLogService.ActionType.CREATE, "Waste", savedWaste.getId(), 
            "Création d'un nouveau déchet: " + savedWaste.getName(), null, null);
        
        return savedWaste;
    }

    /**
     * Récupérer un déchet par ID
     */
    public Optional<Waste> getWasteById(Long id) {
        return wasteRepository.findById(id);
    }

    /**
     * Récupérer un déchet par nom
     */
    public Optional<Waste> getWasteByName(String name) {
        return wasteRepository.findByName(name);
    }

    /**
     * Mettre à jour un déchet
     */
    public Waste updateWaste(Waste waste) {
        Waste existingWaste = wasteRepository.findById(waste.getId())
            .orElseThrow(() -> new RuntimeException("Déchet non trouvé"));
        
        existingWaste.setName(waste.getName());
        existingWaste.setDescription(waste.getDescription());
        existingWaste.setCategory(waste.getCategory());
        existingWaste.setSortingInstructions(waste.getSortingInstructions());
        existingWaste.setBinColor(waste.getBinColor());
        existingWaste.setBinImage(waste.getBinImage());
        existingWaste.setWasteImage(waste.getWasteImage());
        existingWaste.setRecyclable(waste.isRecyclable());
        existingWaste.setHazardous(waste.isHazardous());
        existingWaste.setReuseTips(waste.getReuseTips());
        existingWaste.setRecyclingProcess(waste.getRecyclingProcess());
        
        Waste updatedWaste = wasteRepository.save(existingWaste);
        
        activityLogService.logActivity(null, "ADMIN", 
            ActivityLogService.ActionType.UPDATE, "Waste", waste.getId(), 
            "Modification du déchet: " + waste.getName(), null, null);
        
        return updatedWaste;
    }

    /**
     * Supprimer un déchet
     */
    public void deleteWaste(Long wasteId) {
        Waste waste = wasteRepository.findById(wasteId)
            .orElseThrow(() -> new RuntimeException("Déchet non trouvé"));
        
        wasteRepository.delete(waste);
        
        activityLogService.logActivity(null, "ADMIN", 
            ActivityLogService.ActionType.DELETE, "Waste", wasteId, 
            "Suppression du déchet: " + waste.getName(), null, null);
    }

    /**
     * Lister tous les déchets actifs
     */
    public List<Waste> getAllActiveWaste() {
        return wasteRepository.findByIsActive(true);
    }

    /**
     * Lister tous les déchets
     */
    public List<Waste> getAllWaste() {
        return wasteRepository.findAll();
    }

    /**
     * Récupérer les déchets recyclables
     */
    public List<Waste> getRecyclableWaste() {
        return wasteRepository.findByIsRecyclable(true);
    }

    /**
     * Récupérer les déchets dangereux
     */
    public List<Waste> getHazardousWaste() {
        return wasteRepository.findByIsHazardous(true);
    }

    /**
     * Récupérer les déchets les plus recherchés
     */
    public List<Waste> getMostSearchedWaste() {
        return wasteRepository.findMostSearchedWaste();
    }

    /**
     * Rechercher des déchets
     */
    public List<Waste> searchWaste(String search) {
        List<Waste> results = wasteRepository.searchWaste(search);
        // Incrémenter le compteur de recherche pour le premier résultat
        if (!results.isEmpty()) {
            Waste topResult = results.get(0);
            topResult.setSearchCount((topResult.getSearchCount() != null ? topResult.getSearchCount() : 0L) + 1);
            wasteRepository.save(topResult);
        }
        return results;
    }

    /**
     * Récupérer les déchets par catégorie
     */
    public List<Waste> getWasteByCategory(String category) {
        return wasteRepository.findByCategory(category);
    }
}