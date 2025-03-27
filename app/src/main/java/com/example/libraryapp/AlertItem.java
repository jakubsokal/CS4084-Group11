package com.example.libraryapp;

public class AlertItem {
    private int alertId;
    private int userId;
    private String message;
    private String alertType;
    private boolean isRead;
    private String createdAt;

    public AlertItem(int alertId, int userId, String message, String alertType, boolean isRead, String createdAt) {
        this.alertId = alertId;
        this.userId = userId;
        this.message = message;
        this.alertType = alertType;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public int getAlertId() {
        return alertId;
    }

    public int getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public String getAlertType() {
        return alertType;
    }

    public boolean isRead() {
        return isRead;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
} 