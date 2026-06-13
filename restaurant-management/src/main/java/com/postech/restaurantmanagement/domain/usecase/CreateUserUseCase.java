package com.postech.restaurantmanagement.domain.usecase;

import com.postech.restaurantmanagement.domain.entity.User;
import com.postech.restaurantmanagement.domain.exception.EntityValidationException;
import com.postech.restaurantmanagement.domain.exception.ResourceAlreadyExistsException;
import com.postech.restaurantmanagement.domain.gateway.UserGateway;

/**
 * Use Case responsible for orchestrating the creation of a new system user.
 * Implements Application Business Rules strictly independent of frameworks.
 */
public class CreateUserUseCase {

    private final UserGateway userGateway;

    /**
     * Pure constructor dependency injection forcing decoupling from frameworks.
     *
     * @param userGateway The boundary contract for user data persistence.
     */
    public CreateUserUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    /**
     * Executes the business logic for registering a new user within the platform.
     *
     * @param newUser The user entity data mapped from the presentation layer.
     * @return The persisted User instance populated with generated tracking identifiers.
     * @throws IllegalArgumentException if domain constraints or email uniqueness are violated.
     */
    public User execute(User newUser) {
        // 1. Validates structural enterprise business rules of the entity itself
        if (newUser == null || !newUser.isValid()) {
            throw new EntityValidationException("Invalid user data. Verify structural constraints, email format, and roles.");
        }

        // 2. Enforces application business rule: Email uniqueness
        if (userGateway.findByEmail(newUser.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistsException("A user with this email address already exists.");
        }

        // 3. Delegates persistence orchestration to the infrastructure boundary gateway
        return userGateway.save(newUser);
    }
}