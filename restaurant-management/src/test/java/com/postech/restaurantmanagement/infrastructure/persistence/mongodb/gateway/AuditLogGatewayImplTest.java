package com.postech.restaurantmanagement.infrastructure.persistence.mongodb.gateway;

import com.postech.restaurantmanagement.domain.entity.AuditLog;
import com.postech.restaurantmanagement.infrastructure.persistence.mongodb.entity.AuditLogEntity;
import com.postech.restaurantmanagement.infrastructure.persistence.mongodb.repository.SpringDataMongoAuditRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditLogGatewayImplTest {

    @Mock
    private SpringDataMongoAuditRepository repository;

    @InjectMocks
    private AuditLogGatewayImpl gateway;

    @Test
    void shouldPersistAuditLogDocument() {
        gateway.log("CREATE", "details", "SYSTEM");

        ArgumentCaptor<AuditLogEntity> captor = ArgumentCaptor.forClass(AuditLogEntity.class);
        verify(repository).save(captor.capture());

        AuditLogEntity saved = captor.getValue();
        assertEquals("CREATE", saved.getAction());
        assertEquals("details", saved.getDetails());
        assertEquals("SYSTEM", saved.getPerformedBy());
        assertNotNull(saved.getTimestamp());
    }

    @Test
    void shouldMapFindAllAndFindByActionToDomain() {
        AuditLogEntity entity = new AuditLogEntity("id", "CREATE", "d", LocalDateTime.now(), "SYS");
        when(repository.findAll()).thenReturn(List.of(entity));
        when(repository.findByAction("CREATE")).thenReturn(List.of(entity));

        List<AuditLog> all = gateway.findAll();
        List<AuditLog> byAction = gateway.findByAction("CREATE");

        assertEquals(1, all.size());
        assertEquals("id", all.get(0).getId());
        assertEquals(1, byAction.size());
        assertEquals("CREATE", byAction.get(0).getAction());
    }
}

