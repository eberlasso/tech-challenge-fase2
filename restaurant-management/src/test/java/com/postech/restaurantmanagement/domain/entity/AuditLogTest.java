package com.postech.restaurantmanagement.domain.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuditLogTest {

    @Test
    void shouldBuildAuditLogUsingConstructorAndBuilder() {
        LocalDateTime now = LocalDateTime.now();

        AuditLog direct = new AuditLog("id-1", "ACTION", "details", now, "SYSTEM");
        AuditLog built = AuditLog.builder()
                .id("id-2")
                .action("ACTION_2")
                .details("details-2")
                .timestamp(now)
                .performedBy("USER")
                .build();

        assertEquals("id-1", direct.getId());
        assertEquals("ACTION", direct.getAction());
        assertEquals("details", direct.getDetails());
        assertEquals(now, direct.getTimestamp());
        assertEquals("SYSTEM", direct.getPerformedBy());

        assertEquals("id-2", built.getId());
        assertEquals("ACTION_2", built.getAction());
        assertEquals("details-2", built.getDetails());
        assertEquals(now, built.getTimestamp());
        assertEquals("USER", built.getPerformedBy());
    }
}

