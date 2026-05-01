package com.workshop.platform.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.workshop.platform.entity.WorkshopStatus;

public class WorkshopResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDate sessionDate;
    private LocalTime sessionTime;
    private Integer durationMinutes;
    private String meetingLink;
    private WorkshopStatus status;
    private TeacherSummary teacher;

    public WorkshopResponse() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public LocalTime getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(LocalTime sessionTime) {
        this.sessionTime = sessionTime;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    public WorkshopStatus getStatus() {
        return status;
    }

    public void setStatus(WorkshopStatus status) {
        this.status = status;
    }

    public TeacherSummary getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherSummary teacher) {
        this.teacher = teacher;
    }
}
