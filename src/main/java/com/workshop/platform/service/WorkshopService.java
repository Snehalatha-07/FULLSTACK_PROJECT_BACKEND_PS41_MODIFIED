package com.workshop.platform.service;

import com.workshop.platform.dto.ApprovalRequest;
import com.workshop.platform.dto.WorkshopRequest;
import com.workshop.platform.entity.Role;
import com.workshop.platform.entity.User;
import com.workshop.platform.entity.Workshop;
import com.workshop.platform.entity.WorkshopStatus;
import com.workshop.platform.exception.BadRequestException;
import com.workshop.platform.exception.ResourceNotFoundException;
import com.workshop.platform.repository.UserRepository;
import com.workshop.platform.repository.WorkshopRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkshopService {

    private final WorkshopRepository workshopRepository;
    private final UserRepository userRepository;

    public WorkshopService(WorkshopRepository workshopRepository, UserRepository userRepository) {
        this.workshopRepository = workshopRepository;
        this.userRepository = userRepository;
    }

    @SuppressWarnings("null")
    public Workshop createWorkshop(WorkshopRequest request) {
        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));

        if (teacher.getRole() != Role.TEACHER) {
            throw new BadRequestException("Only teachers can create workshops");
        }

        Workshop workshop = new Workshop();
        workshop.setTitle(normalizeText(request.getTitle(), 120));
        workshop.setDescription(normalizeText(request.getDescription(), 2000));
        workshop.setSessionDate(request.getSessionDate());
        workshop.setSessionTime(request.getSessionTime());
        workshop.setDurationMinutes(request.getDurationMinutes());
        workshop.setMeetingLink(normalizeText(request.getMeetingLink(), 500));
        workshop.setStatus(WorkshopStatus.PENDING);
        workshop.setTeacher(teacher);

        return workshopRepository.save(workshop);
    }

    public List<Workshop> getAllWorkshops() {
        return workshopRepository.searchWorkshops(null, null, null);
    }

    public List<Workshop> getApprovedWorkshops() {
        return workshopRepository.searchWorkshops(null, null, WorkshopStatus.APPROVED);
    }

    @SuppressWarnings("null")
    public List<Workshop> getTeacherWorkshops(Long teacherId) {
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found"));
        return workshopRepository.searchWorkshops(null, teacher.getId(), null);
    }

    public List<Workshop> searchWorkshops(String search, Long teacherId, WorkshopStatus status) {
        return workshopRepository.searchWorkshops(escapeLikeSearch(search), teacherId, status);
    }

    @SuppressWarnings("null")
    public Workshop updateWorkshop(Long workshopId, WorkshopRequest request) {
        Workshop workshop = workshopRepository.findById(workshopId)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop not found"));

        if (!workshop.getTeacher().getId().equals(request.getTeacherId())) {
            throw new BadRequestException("Only workshop owner can update it");
        }

        workshop.setTitle(normalizeText(request.getTitle(), 120));
        workshop.setDescription(normalizeText(request.getDescription(), 2000));
        workshop.setSessionDate(request.getSessionDate());
        workshop.setSessionTime(request.getSessionTime());
        workshop.setDurationMinutes(request.getDurationMinutes());
        workshop.setMeetingLink(normalizeText(request.getMeetingLink(), 500));
        workshop.setStatus(WorkshopStatus.PENDING);

        return workshopRepository.save(workshop);
    }

    @SuppressWarnings("null")
    public void deleteWorkshop(Long workshopId, Long teacherId) {
        Workshop workshop = workshopRepository.findById(workshopId)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop not found"));

        if (!workshop.getTeacher().getId().equals(teacherId)) {
            throw new BadRequestException("Only workshop owner can delete it");
        }

        workshopRepository.delete(workshop);
    }

    @SuppressWarnings("null")
    public Workshop approveOrRejectWorkshop(Long workshopId, ApprovalRequest request) {
        User admin = userRepository.findById(request.getAdminId())
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found"));

        if (admin.getRole() != Role.ADMIN) {
            throw new BadRequestException("Only admins can approve or reject workshops");
        }

        Workshop workshop = workshopRepository.findById(workshopId)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop not found"));

        if (request.getStatus() != WorkshopStatus.APPROVED && request.getStatus() != WorkshopStatus.REJECTED) {
            throw new BadRequestException("Status must be APPROVED or REJECTED");
        }

        workshop.setStatus(request.getStatus());
        return workshopRepository.save(workshop);
    }

    @SuppressWarnings("null")
    public Workshop completeWorkshop(Long workshopId, Long teacherId) {
        Workshop workshop = workshopRepository.findById(workshopId)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop not found"));

        if (!workshop.getTeacher().getId().equals(teacherId)) {
            throw new BadRequestException("Only workshop owner can complete it");
        }

        workshop.setStatus(WorkshopStatus.COMPLETED);
        return workshopRepository.save(workshop);
    }

    @SuppressWarnings("null")
    public Workshop getById(Long workshopId) {
        return workshopRepository.findById(workshopId)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop not found"));
    }

    private String normalizeText(String input, int maxLen) {
        if (input == null) {
            return null;
        }

        String normalized = input
                .replaceAll("[\\p{Cntrl}]", " ")
                .trim()
                .replaceAll("\\s+", " ");

        if (normalized.length() > maxLen) {
            throw new BadRequestException("Input is too long");
        }

        return normalized;
    }

    private String escapeLikeSearch(String search) {
        if (search == null) {
            return null;
        }

        String normalized = normalizeText(search, 120);
        if (normalized.isEmpty()) {
            return normalized;
        }

        return normalized
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }
}
