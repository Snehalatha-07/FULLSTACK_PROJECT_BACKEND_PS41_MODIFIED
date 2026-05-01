package com.workshop.platform.dto;

import java.time.LocalDateTime;

public class FeedbackResponse {
    private Long id;
    private WorkshopResponse workshop;
    private UserSummary student;
    private Integer rating;
    private String comments;
    private LocalDateTime submittedAt;

    public FeedbackResponse() {}

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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}
