package com.postech.restaurantmanagement.infrastructure.persistence.mongodb.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;

/**
 * Structural MongoDB Document mapping application audit trails and historical logs.
 */
@Document(collection = "audit_logs")
public class AuditLogEntity {

    @Id
    private String id;

    @Field("action")
    private String action;

    @Field("details")
    private String details;

    @Field("timestamp")
    private LocalDateTime timestamp;

    @Field("performed_by")
    private String performedBy;

    public AuditLogEntity() {}

    public AuditLogEntity(String id, String action, String details, LocalDateTime timestamp, String performedBy) {
        this.id = id;
        this.action = action;
        this.details = details;
        this.timestamp = timestamp;
        this.performedBy = performedBy;
    }

    // --- GETTERS AND SETTERS ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getPerformedBy() { return performedBy; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }
}