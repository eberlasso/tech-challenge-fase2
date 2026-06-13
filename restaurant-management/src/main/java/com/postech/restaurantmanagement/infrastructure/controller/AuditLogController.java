package com.postech.restaurantmanagement.infrastructure.controller;

import com.postech.restaurantmanagement.domain.usecase.GetAuditLogsUseCase;
import com.postech.restaurantmanagement.infrastructure.controller.dto.AuditLogResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Interface Adapter REST Controller exposing endpoints to inspect MongoDB historical audit logs.
 */
@RestController
@RequestMapping("/api/v1/audit-logs")
public class AuditLogController {

    private final GetAuditLogsUseCase getAuditLogsUseCase;

    public AuditLogController(GetAuditLogsUseCase getAuditLogsUseCase) {
        this.getAuditLogsUseCase = getAuditLogsUseCase;
    }

    @GetMapping
    public ResponseEntity<List<AuditLogResponse>> getAllLogs(@RequestParam(required = false) String action) {
        List<AuditLogResponse> logs = getAuditLogsUseCase.executeByAction(action).stream()
                .map(log -> new AuditLogResponse(
                        log.getId(),
                        log.getAction(),
                        log.getDetails(),
                        log.getTimestamp(),
                        log.getPerformedBy()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(logs);
    }
}