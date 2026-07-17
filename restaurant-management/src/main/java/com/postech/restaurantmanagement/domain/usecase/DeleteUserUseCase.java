package com.postech.restaurantmanagement.domain.usecase;

import com.postech.restaurantmanagement.domain.entity.User;
import com.postech.restaurantmanagement.domain.exception.ResourceNotFoundException;
import com.postech.restaurantmanagement.domain.gateway.AuditLogGateway;
import com.postech.restaurantmanagement.domain.gateway.UserGateway;

public class DeleteUserUseCase {

    private final UserGateway userGateway;
    private final AuditLogGateway auditLogGateway;

    public DeleteUserUseCase(UserGateway userGateway, AuditLogGateway auditLogGateway) {
        this.userGateway = userGateway;
        this.auditLogGateway = auditLogGateway;
    }

    public void execute(Long id) {
        User user = userGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        userGateway.deleteById(id);

        auditLogGateway.log(
                "USER_DELETE",
                String.format("User '%s' (ID %d) removed.", user.getName(), user.getId()),
                "SYSTEM_USER"
        );
    }
}
