package com.workshop.platform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MaterialRequest {
    @NotNull
    private Long workshopId;

    @NotNull
    private Long teacherId;

    @NotBlank
    private String title;

    @NotBlank
    private String type;

    @NotBlank
    private String url;

    @NotNull
    private Boolean postSession;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getPostSession() {
        return postSession;
    }

    public void setPostSession(Boolean postSession) {
        this.postSession = postSession;
    }
}
