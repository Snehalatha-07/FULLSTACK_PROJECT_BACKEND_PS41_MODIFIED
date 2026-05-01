package com.workshop.platform.dto;

import com.workshop.platform.entity.WorkshopStatus;
import jakarta.validation.constraints.NotNull;

public class ApprovalRequest {
    @NotNull
    private Long adminId;

    @NotNull
    private WorkshopStatus status;

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public WorkshopStatus getStatus() {
        return status;
    }

    public void setStatus(WorkshopStatus status) {
        this.status = status;
    }
}
