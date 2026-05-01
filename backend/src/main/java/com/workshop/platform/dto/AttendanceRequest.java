package com.workshop.platform.dto;

import jakarta.validation.constraints.NotNull;

public class AttendanceRequest {
    @NotNull
    private Long workshopId;

    @NotNull
    private Long teacherId;

    @NotNull
    private Long studentId;

    @NotNull
    private Boolean present;

    public Long getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(Long workshopId) {
        this.workshopId = workshopId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Boolean getPresent() {
        return present;
    }

    public void setPresent(Boolean present) {
        this.present = present;
    }
}
