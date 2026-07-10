package com.recycling.controller;

import com.recycling.model.Report;
import com.recycling.model.ReportIntervention;
import com.recycling.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Contrôleur pour la gestion des signalements
 */
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /**
     * Créer un nouveau signalement (Citoyen)
     */
    @PostMapping("/citizen")
    public ResponseEntity<Report> createReport(@Valid @RequestBody Report report) {
        Report createdReport = reportService.createReport(report);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReport);
    }

    /**
     * Récupérer tous les signalements (Admin)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    /**
     * Récupérer un signalement par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Report> getReportById(@PathVariable Long id) {
        Report report = reportService.getReportById(id)
                .orElseThrow(() -> new RuntimeException("Signalement non trouvé"));
        return ResponseEntity.ok(report);
    }

    /**
     * Rechercher des signalements
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<Report>> searchReports(@RequestParam String query) {
        List<Report> reports = reportService.searchReports(query);
        return ResponseEntity.ok(reports);
    }

    /**
     * Récupérer les signalements par statut
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<Report>> getReportsByStatus(@PathVariable String status) {
        Report.ReportStatus reportStatus = Report.ReportStatus.valueOf(status);
        List<Report> reports = reportService.getReportsByStatus(reportStatus);
        return ResponseEntity.ok(reports);
    }

    /**
     * Récupérer les signalements par catégorie
     */
    @GetMapping("/category/{category}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<List<Report>> getReportsByCategory(@PathVariable String category) {
        Report.ReportCategory reportCategory = Report.ReportCategory.valueOf(category);
        List<Report> reports = reportService.getReportsByCategory(reportCategory);
        return ResponseEntity.ok(reports);
    }

    /**
     * Mettre à jour un signalement
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Report> updateReport(@PathVariable Long id, @Valid @RequestBody Report report) {
        report.setId(id);
        Report updatedReport = reportService.updateReport(report);
        return ResponseEntity.ok(updatedReport);
    }

    /**
     * Changer le statut d'un signalement
     */
    @PostMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Report> changeReportStatus(@PathVariable Long id,
                                                     @RequestParam String status,
                                                     @RequestParam(required = false) String notes) {
        Report.ReportStatus reportStatus = Report.ReportStatus.valueOf(status);
        Report updatedReport = reportService.changeReportStatus(id, reportStatus, notes);
        return ResponseEntity.ok(updatedReport);
    }

    /**
     * Affecter un signalement à une équipe
     */
    @PostMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<Report> assignReport(@PathVariable Long id,
                                               @RequestParam Long userId,
                                               @RequestParam String teamName) {
        Report updatedReport = reportService.assignReportToTeam(id, userId, teamName);
        return ResponseEntity.ok(updatedReport);
    }

    /**
     * Ajouter une intervention au signalement
     */
    @PostMapping("/{id}/intervention")
    @PreAuthorize("hasRole('FIELD_TEAM') or hasRole('MANAGER')")
    public ResponseEntity<ReportIntervention> addIntervention(@PathVariable Long id,
                                                              @Valid @RequestBody ReportIntervention intervention) {
        ReportIntervention addedIntervention = reportService.addIntervention(id, intervention);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedIntervention);
    }

    /**
     * Compter les nouveaux signalements
     */
    @GetMapping("/stats/new-count")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> countNewReports() {
        long count = reportService.countNewReports();
        return ResponseEntity.ok(new java.util.HashMap<String, Long>()
            .put("newCount", count)
        );
    }

    /**
     * Compter les signalements résolus
     */
    @GetMapping("/stats/resolved-count")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<?> countResolvedReports() {
        long count = reportService.countResolvedReports();
        return ResponseEntity.ok(new java.util.HashMap<String, Long>()
            .put("resolvedCount", count)
        );
    }

    /**
     * Supprimer un signalement
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.ok(new java.util.HashMap<String, String>()
            .put("message", "Signalement supprimé")
        );
    }
}