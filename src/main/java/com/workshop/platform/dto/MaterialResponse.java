package com.workshop.platform.dto;

public class MaterialResponse {
    private Long id;
    private String title;
    private String type;
    private String url;
    private Boolean postSession;
    private Long workshopId;
    private UserSummary uploadedBy;

    public MaterialResponse() {}

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

    public Long getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(Long workshopId) {
        this.workshopId = workshopId;
    }

    public UserSummary getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(UserSummary uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
}
