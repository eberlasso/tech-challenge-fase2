package com.postech.restaurantmanagement.domain.entity;

import java.time.LocalDateTime;

public class AuditLog {
    private final String id;
    private final String action;
    private final String details;
    private final LocalDateTime timestamp;
    private final String performedBy;

    public AuditLog(String id, String action, String details, LocalDateTime timestamp, String performedBy) {
        this.id = id;
        this.action = action;
        this.details = details;
        this.timestamp = timestamp;
        this.performedBy = performedBy;
    }

    // --- GETTERS ---
    public String getId() { return id; }
    public String getAction() { return action; }
    public String getDetails() { return details; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getPerformedBy() { return performedBy; }

    // Builder manual simples para manter o padrão do domínio puro
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private String action;
        private String details;
        private LocalDateTime timestamp;
        private String performedBy;

        public Builder id(String id) { this.id = id; return this; }
        public Builder action(String action) { this.action = action; return this; }
        public Builder details(String details) { this.details = details; return this; }
        public Builder timestamp(LocalDateTime timestamp) { this.timestamp = timestamp; return this; }
        public Builder performedBy(String performedBy) { this.performedBy = performedBy; return this; }

        public AuditLog build() {
            return new AuditLog(id, action, details, timestamp, performedBy);
        }
    }
}