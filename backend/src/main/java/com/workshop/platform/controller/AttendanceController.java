package com.workshop.platform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workshop.platform.dto.AttendanceRequest;
import com.workshop.platform.dto.AttendanceResponse;
import com.workshop.platform.dto.UserSummary;
import com.workshop.platform.dto.WorkshopResponse;
import com.workshop.platform.entity.Attendance;
import com.workshop.platform.entity.User;
import com.workshop.platform.service.AttendanceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping
    public ResponseEntity<AttendanceResponse> markAttendance(@Valid @RequestBody AttendanceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(attendanceService.markAttendance(request)));
    }

    @GetMapping("/workshop/{workshopId}")
    public ResponseEntity<List<AttendanceResponse>> getWorkshopAttendance(@PathVariable Long workshopId) {
        return ResponseEntity.ok(attendanceService.getWorkshopAttendance(workshopId).stream().map(this::toDto).toList());
    }

    private AttendanceResponse toDto(Attendance attendance) {
        if (attendance == null) {
            return null;
        }

        AttendanceResponse response = new AttendanceResponse();
        response.setId(attendance.getId());
        response.setPresent(attendance.getPresent());
        response.setMarkedAt(attendance.getMarkedAt());

        try {
            if (attendance.getWorkshop() != null) {
                WorkshopResponse workshop = new WorkshopResponse();
                workshop.setId(attendance.getWorkshop().getId());
                workshop.setTitle(attendance.getWorkshop().getTitle());
                response.setWorkshop(workshop);
            }
        } catch (Exception ignore) {
        }

        try {
            if (attendance.getStudent() != null) {
                response.setStudent(toSummary(attendance.getStudent()));
            }
        } catch (Exception ignore) {
        }

        try {
            if (attendance.getMarkedBy() != null) {
                response.setMarkedBy(toSummary(attendance.getMarkedBy()));
            }
        } catch (Exception ignore) {
        }

        return response;
    }

    private UserSummary toSummary(User user) {
        if (user == null) {
            return null;
        }

        String role = user.getRole() != null ? user.getRole().name() : null;
        return new UserSummary(user.getId(), user.getName(), user.getEmail(), role);
    }
}
