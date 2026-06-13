package com.postech.restaurantmanagement.infrastructure.persistence.mongodb.repository;

import com.postech.restaurantmanagement.infrastructure.persistence.mongodb.entity.AuditLogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Native Spring Data MongoDB Repository contract exposing document-based operations for Auditing.
 */
@Repository
public interface SpringDataMongoAuditRepository extends MongoRepository<AuditLogEntity, String> {

    // Lista histórica de auditoria baseada no tipo de ação executada
    List<AuditLogEntity> findByAction(String action);
}