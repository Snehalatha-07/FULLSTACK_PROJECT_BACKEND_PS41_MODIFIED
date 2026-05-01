package com.workshop.platform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.workshop.platform.dto.DashboardStatsResponse;
import com.workshop.platform.dto.UserSummary;
import com.workshop.platform.entity.User;
import com.workshop.platform.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserSummary>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers().stream().map(this::toSummary).toList());
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<UserSummary>> getTeachers() {
        return ResponseEntity.ok(adminService.getTeachers().stream().map(this::toSummary).toList());
    }

    @GetMapping("/students")
    public ResponseEntity<List<UserSummary>> getStudents() {
        return ResponseEntity.ok(adminService.getStudents().stream().map(this::toSummary).toList());
    }

    @PostMapping("/teachers")
    public ResponseEntity<UserSummary> addTeacher(@RequestParam Long adminId, @RequestBody User teacher) {
        return ResponseEntity.status(HttpStatus.CREATED).body(toSummary(adminService.addTeacher(adminId, teacher)));
    }

    @DeleteMapping("/teachers/{teacherId}")
    public ResponseEntity<Void> removeTeacher(@RequestParam Long adminId, @PathVariable Long teacherId) {
        adminService.removeTeacher(adminId, teacherId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/students/{studentId}")
    public ResponseEntity<Void> removeStudent(@RequestParam Long adminId, @PathVariable Long studentId) {
        adminService.removeStudent(adminId, studentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsResponse> getStats(@RequestParam Long adminId) {
        return ResponseEntity.ok(adminService.getStats(adminId));
    }

    private UserSummary toSummary(User user) {
        if (user == null) {
            return null;
        }

        String role = user.getRole() != null ? user.getRole().name() : null;
        return new UserSummary(user.getId(), user.getName(), user.getEmail(), role);
    }
}
