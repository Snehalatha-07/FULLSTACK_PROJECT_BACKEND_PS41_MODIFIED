package com.workshop.platform.dto;

import java.time.LocalDateTime;

public class RegistrationResponse {
    private Long id;
    private UserSummary user;
    private WorkshopResponse workshop;
    private LocalDateTime registeredAt;

    public RegistrationResponse() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserSummary getUser() {
        return user;
    }

    public void setUser(UserSummary user) {
        this.user = user;
    }

    public WorkshopResponse getWorkshop() {
        return workshop;
    }

    public void setWorkshop(WorkshopResponse workshop) {
        this.workshop = workshop;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }
}
