package com.workshop.platform.dto;

import java.time.LocalDateTime;

public class AttendanceResponse {
    private Long id;
    private WorkshopResponse workshop;
    private UserSummary student;
    private UserSummary markedBy;
    private Boolean present;
    private LocalDateTime markedAt;

    public AttendanceResponse() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkshopResponse getWorkshop() {
        return workshop;
    }

    public void setWorkshop(WorkshopResponse workshop) {
        this.workshop = workshop;
    }

    public UserSummary getStudent() {
        return student;
    }

    public void setStudent(UserSummary student) {
        this.student = student;
    }

    public UserSummary getMarkedBy() {
        return markedBy;
    }

    public void setMarkedBy(UserSummary markedBy) {
        this.markedBy = markedBy;
    }

    public Boolean getPresent() {
        return present;
    }

    public void setPresent(Boolean present) {
        this.present = present;
    }

    public LocalDateTime getMarkedAt() {
        return markedAt;
    }

    public void setMarkedAt(LocalDateTime markedAt) {
        this.markedAt = markedAt;
    }
}