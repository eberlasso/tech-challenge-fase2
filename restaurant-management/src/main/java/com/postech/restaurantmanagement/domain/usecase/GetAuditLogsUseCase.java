package com.postech.restaurantmanagement.domain.usecase;

import com.postech.restaurantmanagement.domain.entity.AuditLog;
import com.postech.restaurantmanagement.domain.gateway.AuditLogGateway;
import java.util.List;

/**
 * Orchestrator Use Case handling application mechanics for retrieving audit log history.
 */
public class GetAuditLogsUseCase {

    private final AuditLogGateway auditLogGateway;

    public GetAuditLogsUseCase(AuditLogGateway auditLogGateway) {
        this.auditLogGateway = auditLogGateway;
    }

    public List<AuditLog> executeAll() {
        return auditLogGateway.findAll();
    }

    public List<AuditLog> executeByAction(String action) {
        if (action == null || action.isBlank()) {
            return auditLogGateway.findAll();
        }
        return auditLogGateway.findByAction(action);
    }
}