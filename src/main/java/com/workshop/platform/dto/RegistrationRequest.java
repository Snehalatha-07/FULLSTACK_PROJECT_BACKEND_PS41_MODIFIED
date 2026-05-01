package com.workshop.platform.dto;

import jakarta.validation.constraints.NotNull;

public class RegistrationRequest {
    @NotNull
    private Long workshopId;

    @NotNull
    private Long userId;

    public Long getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(Long workshopId) {
        this.workshopId = workshopId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
