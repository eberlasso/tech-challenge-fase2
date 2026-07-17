package com.postech.restaurantmanagement.domain.usecase;

import com.postech.restaurantmanagement.domain.entity.User;
import com.postech.restaurantmanagement.domain.exception.EntityValidationException;
import com.postech.restaurantmanagement.domain.exception.ResourceAlreadyExistsException;
import com.postech.restaurantmanagement.domain.exception.ResourceNotFoundException;
import com.postech.restaurantmanagement.domain.gateway.AuditLogGateway;
import com.postech.restaurantmanagement.domain.gateway.UserGateway;

public class UpdateUserUseCase {

    private final UserGateway userGateway;
    private final AuditLogGateway auditLogGateway;

    public UpdateUserUseCase(UserGateway userGateway, AuditLogGateway auditLogGateway) {
        this.userGateway = userGateway;
        this.auditLogGateway = auditLogGateway;
    }

    public User execute(User userToUpdate) {
        if (userToUpdate == null || userToUpdate.getId() == null || !userToUpdate.isValid()) {
            throw new EntityValidationException("Invalid user data. Verify structural constraints, email format, and roles.");
        }

        User currentUser = userGateway.findById(userToUpdate.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userToUpdate.getId()));

        if (userGateway.existsByEmailAndIdNot(userToUpdate.getEmail(), userToUpdate.getId())) {
            throw new ResourceAlreadyExistsException("A user with this email address already exists.");
        }

        User updatedUser = userGateway.save(userToUpdate);

        auditLogGateway.log(
                "USER_UPDATE",
                String.format("User '%s' (ID %d) updated. Previous email: %s, new email: %s.",
                        updatedUser.getName(), updatedUser.getId(), currentUser.getEmail(), updatedUser.getEmail()),
                "SYSTEM_USER"
        );

        return updatedUser;
    }
}
