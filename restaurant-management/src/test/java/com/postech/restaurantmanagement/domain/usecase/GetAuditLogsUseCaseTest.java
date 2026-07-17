package com.postech.restaurantmanagement.domain.usecase;

import com.postech.restaurantmanagement.domain.entity.AuditLog;
import com.postech.restaurantmanagement.domain.gateway.AuditLogGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAuditLogsUseCaseTest {

    @Mock
    private AuditLogGateway auditLogGateway;

    @InjectMocks
    private GetAuditLogsUseCase useCase;

    @Test
    void shouldReturnAllLogs() {
        List<AuditLog> logs = List.of(new AuditLog("1", "A", "D", LocalDateTime.now(), "SYS"));
        when(auditLogGateway.findAll()).thenReturn(logs);

        List<AuditLog> result = useCase.executeAll();

        assertEquals(1, result.size());
        verify(auditLogGateway).findAll();
    }

    @Test
    void shouldReturnAllLogsWhenActionIsNullOrBlank() {
        when(auditLogGateway.findAll()).thenReturn(List.of());

        useCase.executeByAction(null);
        useCase.executeByAction(" ");

        verify(auditLogGateway, times(2)).findAll();
        verify(auditLogGateway, never()).findByAction(anyString());
    }

    @Test
    void shouldReturnFilteredLogsWhenActionProvided() {
        when(auditLogGateway.findByAction("CREATE_USER")).thenReturn(List.of());

        useCase.executeByAction("CREATE_USER");

        verify(auditLogGateway).findByAction("CREATE_USER");
    }
}

