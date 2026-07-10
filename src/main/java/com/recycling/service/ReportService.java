package com.recycling.service;

import com.recycling.model.Report;
import com.recycling.model.ReportIntervention;
import com.recycling.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des signalements
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {

    private final ReportRepository reportRepository;
    private final ActivityLogService activityLogService;

    /**
     * Créer un nouveau signalement
     */
    public Report createReport(Report report) {
        report.setStatus(Report.ReportStatus.NEW);
        Report savedReport = reportRepository.save(report);
        
        activityLogService.logActivity(null, "SYSTEM", 
            ActivityLogService.ActionType.CREATE, "Report", savedReport.getId(), 
            "Nouveau signalement créé: " + savedReport.getTitle(), null, null);
        
        return savedReport;
    }

    /**
     * Récupérer un signalement par ID
     */
    public Optional<Report> getReportById(Long id) {
        return reportRepository.findById(id);
    }

    /**
     * Mettre à jour un signalement
     */
    public Report updateReport(Report report) {
        Report existingReport = reportRepository.findById(report.getId())
            .orElseThrow(() -> new RuntimeException("Signalement non trouvé"));
        
        existingReport.setTitle(report.getTitle());
        existingReport.setDescription(report.getDescription());
        existingReport.setCategory(report.getCategory());
        existingReport.setCitizenName(report.getCitizenName());
        existingReport.setCitizenEmail(report.getCitizenEmail());
        existingReport.setCitizenPhone(report.getCitizenPhone());
        existingReport.setLocationAddress(report.getLocationAddress());
        existingReport.setLatitude(report.getLatitude());
        existingReport.setLongitude(report.getLongitude());
        existingReport.setPhotoUrl(report.getPhotoUrl());
        
        Report updatedReport = reportRepository.save(existingReport);
        
        activityLogService.logActivity(null, "ADMIN", 
            ActivityLogService.ActionType.UPDATE, "Report", report.getId(), 
            "Mise à jour du signalement: " + report.getTitle(), null, null);
        
        return updatedReport;
    }

    /**
     * Changer le statut d'un signalement
     */
    public Report changeReportStatus(Long reportId, Report.ReportStatus newStatus, String notes) {
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new RuntimeException("Signalement non trouvé"));
        
        report.setStatus(newStatus);
        if (newStatus == Report.ReportStatus.RESOLVED) {
            report.setResolvedAt(LocalDateTime.now());
        }
        report.setResolutionNotes(notes);
        
        Report updatedReport = reportRepository.save(report);
        
        activityLogService.logActivity(null, "ADMIN", 
            ActivityLogService.ActionType.UPDATE, "Report", reportId, 
            "Changement de statut du signalement en: " + newStatus.getDisplayName(), null, null);
        
        return updatedReport;
    }

    /**
     * Affecter un signalement à une équipe
     */
    public Report assignReportToTeam(Long reportId, Long userId, String teamName) {
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new RuntimeException("Signalement non trouvé"));
        
        report.setAssignedToUserId(userId);
        report.setAssignedTeam(teamName);
        report.setStatus(Report.ReportStatus.IN_PROGRESS);
        
        Report updatedReport = reportRepository.save(report);
        
        activityLogService.logActivity(userId, "ADMIN", 
            ActivityLogService.ActionType.UPDATE, "Report", reportId, 
            "Affectation du signalement à l'équipe: " + teamName, null, null);
        
        return updatedReport;
    }

    /**
     * Ajouter une intervention au signalement
     */
    public ReportIntervention addIntervention(Long reportId, ReportIntervention intervention) {
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new RuntimeException("Signalement non trouvé"));
        
        intervention.setReport(report);
        report.getInterventions().add(intervention);
        reportRepository.save(report);
        
        return intervention;
    }

    /**
     * Supprimer un signalement
     */
    public void deleteReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new RuntimeException("Signalement non trouvé"));
        
        reportRepository.delete(report);
        
        activityLogService.logActivity(null, "ADMIN", 
            ActivityLogService.ActionType.DELETE, "Report", reportId, 
            "Suppression du signalement: " + report.getTitle(), null, null);
    }

    /**
     * Récupérer les signalements par statut
     */
    public List<Report> getReportsByStatus(Report.ReportStatus status) {
        return reportRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    /**
     * Récupérer les signalements par catégorie
     */
    public List<Report> getReportsByCategory(Report.ReportCategory category) {
        return reportRepository.findByCategory(category);
    }

    /**
     * Récupérer tous les signalements
     */
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    /**
     * Compter les nouveaux signalements
     */
    public long countNewReports() {
        return reportRepository.countNewReports();
    }

    /**
     * Compter les signalements résolus
     */
    public long countResolvedReports() {
        return reportRepository.countResolvedReports();
    }

    /**
     * Rechercher des signalements
     */
    public List<Report> searchReports(String search) {
        return reportRepository.searchReports(search);
    }

    /**
     * Récupérer les signalements d'une période
     */
    public List<Report> getReportsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return reportRepository.findReportsByDateRange(startDate, endDate);
    }
}