package com.postech.restaurantmanagement.infrastructure.persistence.mongodb.gateway;

import com.postech.restaurantmanagement.domain.entity.AuditLog;
import com.postech.restaurantmanagement.domain.gateway.AuditLogGateway;
import com.postech.restaurantmanagement.infrastructure.persistence.mongodb.entity.AuditLogEntity;
import com.postech.restaurantmanagement.infrastructure.persistence.mongodb.repository.SpringDataMongoAuditRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Infrastructure implementation adapting the clean Domain Audit Gateway to the MongoDB cluster.
 */
@Component
public class AuditLogGatewayImpl implements AuditLogGateway {

    private final SpringDataMongoAuditRepository mongoRepository;

    public AuditLogGatewayImpl(SpringDataMongoAuditRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public void log(String action, String details, String performedBy) {
        AuditLogEntity auditDocument = new AuditLogEntity(
                null,
                action,
                details,
                LocalDateTime.now(),
                performedBy
        );
        mongoRepository.save(auditDocument);
    }

    @Override
    public List<AuditLog> findAll() {
        return mongoRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditLog> findByAction(String action) {
        return mongoRepository.findByAction(action).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private AuditLog toDomain(AuditLogEntity entity) {
        return AuditLog.builder()
                .id(entity.getId())
                .action(entity.getAction())
                .details(entity.getDetails())
                .timestamp(entity.getTimestamp())
                .performedBy(entity.getPerformedBy())
                .build();
    }
}