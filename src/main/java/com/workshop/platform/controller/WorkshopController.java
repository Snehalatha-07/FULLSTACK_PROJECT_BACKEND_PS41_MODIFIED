package com.workshop.platform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.workshop.platform.dto.ApprovalRequest;
import com.workshop.platform.dto.TeacherSummary;
import com.workshop.platform.dto.WorkshopRequest;
import com.workshop.platform.dto.WorkshopResponse;
import com.workshop.platform.entity.Workshop;
import com.workshop.platform.entity.WorkshopStatus;
import com.workshop.platform.service.WorkshopService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/workshops")
public class WorkshopController {

    private final WorkshopService workshopService;

    public WorkshopController(WorkshopService workshopService) {
        this.workshopService = workshopService;
    }

    @PostMapping
    public ResponseEntity<WorkshopResponse> createWorkshop(@Valid @RequestBody WorkshopRequest request) {
        Workshop created = workshopService.createWorkshop(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(created));
    }

    @GetMapping
    public ResponseEntity<List<WorkshopResponse>> getAllWorkshops(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) WorkshopStatus status) {
        List<Workshop> list = workshopService.searchWorkshops(search, teacherId, status);
        return ResponseEntity.ok(list.stream().map(this::toDto).toList());
    }

    @GetMapping("/approved")
    public ResponseEntity<List<WorkshopResponse>> getApprovedWorkshops(@RequestParam(required = false) String search) {
        List<Workshop> list = workshopService.searchWorkshops(search, null, WorkshopStatus.APPROVED);
        return ResponseEntity.ok(list.stream().map(this::toDto).toList());
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<WorkshopResponse>> getTeacherWorkshops(@PathVariable Long teacherId, @RequestParam(required = false) String search) {
        List<Workshop> list = workshopService.searchWorkshops(search, teacherId, null);
        return ResponseEntity.ok(list.stream().map(this::toDto).toList());
    }

    @GetMapping("/{workshopId}")
    public ResponseEntity<WorkshopResponse> getById(@PathVariable Long workshopId) {
        Workshop w = workshopService.getById(workshopId);
        return ResponseEntity.ok(toDto(w));
    }

    @PutMapping("/{workshopId}")
    public ResponseEntity<WorkshopResponse> updateWorkshop(@PathVariable Long workshopId, @Valid @RequestBody WorkshopRequest request) {
        Workshop w = workshopService.updateWorkshop(workshopId, request);
        return ResponseEntity.ok(toDto(w));
    }

    @DeleteMapping("/{workshopId}")
    public ResponseEntity<Void> deleteWorkshop(@PathVariable Long workshopId, @RequestParam Long teacherId) {
        workshopService.deleteWorkshop(workshopId, teacherId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{workshopId}/approval")
    public ResponseEntity<WorkshopResponse> approveOrRejectWorkshop(@PathVariable Long workshopId, @Valid @RequestBody ApprovalRequest request) {
        Workshop w = workshopService.approveOrRejectWorkshop(workshopId, request);
        return ResponseEntity.ok(toDto(w));
    }

    @PatchMapping("/{workshopId}/complete")
    public ResponseEntity<WorkshopResponse> completeWorkshop(@PathVariable Long workshopId, @RequestParam Long teacherId) {
        Workshop w = workshopService.completeWorkshop(workshopId, teacherId);
        return ResponseEntity.ok(toDto(w));
    }

    private WorkshopResponse toDto(Workshop w) {
        if (w == null) return null;
        WorkshopResponse r = new WorkshopResponse();
        r.setId(w.getId());
        r.setTitle(w.getTitle());
        r.setDescription(w.getDescription());
        r.setSessionDate(w.getSessionDate());
        r.setSessionTime(w.getSessionTime());
        r.setDurationMinutes(w.getDurationMinutes());
        r.setMeetingLink(w.getMeetingLink());
        r.setStatus(w.getStatus());
        if (w.getTeacher() != null) {
            try {
                TeacherSummary t = new TeacherSummary(w.getTeacher().getId(), w.getTeacher().getName(), w.getTeacher().getEmail());
                r.setTeacher(t);
            } catch (Exception ex) {
                // If teacher proxy cannot be initialized, only set id if available
                try {
                    TeacherSummary t = new TeacherSummary(w.getTeacher().getId(), null, null);
                    r.setTeacher(t);
                } catch (Exception ignore) {
                }
            }
        }
        return r;
    }
}
