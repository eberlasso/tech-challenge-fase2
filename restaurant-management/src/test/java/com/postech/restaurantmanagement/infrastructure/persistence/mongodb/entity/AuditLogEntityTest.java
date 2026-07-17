package com.postech.restaurantmanagement.infrastructure.persistence.mongodb.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuditLogEntityTest {

    @Test
    void shouldUseConstructorsAndAccessors() {
        LocalDateTime now = LocalDateTime.now();

        AuditLogEntity entity = new AuditLogEntity();
        entity.setId("id");
        entity.setAction("CREATE");
        entity.setDetails("details");
        entity.setTimestamp(now);
        entity.setPerformedBy("SYSTEM");

        assertEquals("id", entity.getId());
        assertEquals("CREATE", entity.getAction());
        assertEquals("details", entity.getDetails());
        assertEquals(now, entity.getTimestamp());
        assertEquals("SYSTEM", entity.getPerformedBy());

        AuditLogEntity allArgs = new AuditLogEntity("id2", "UPDATE", "d2", now, "USER");
        assertEquals("id2", allArgs.getId());
        assertEquals("UPDATE", allArgs.getAction());
    }
}

