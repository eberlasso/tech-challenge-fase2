package com.postech.restaurantmanagement.domain.gateway;

import com.postech.restaurantmanagement.domain.entity.AuditLog;
import java.util.List;

/**
 * Domain boundary contract governing the operational requirements for system auditing.
 */
public interface AuditLogGateway {

    void log(String action, String details, String performedBy);

    List<AuditLog> findAll();

    List<AuditLog> findByAction(String action);
}