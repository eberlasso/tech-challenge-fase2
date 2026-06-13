package com.postech.restaurantmanagement.infrastructure.controller;

import com.postech.restaurantmanagement.infrastructure.controller.api.UserApi;
import com.postech.restaurantmanagement.infrastructure.controller.dto.CreateUserRequest;
import com.postech.restaurantmanagement.infrastructure.controller.dto.UserResponse;
import com.postech.restaurantmanagement.domain.entity.User;
import com.postech.restaurantmanagement.domain.entity.UserRole;
import com.postech.restaurantmanagement.domain.usecase.CreateUserUseCase;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Interface Adapter REST Controller exposing endpoints for User management.
 * Dispatches HTTP requests to core application business rules use cases.
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController implements UserApi{

    private final CreateUserUseCase createUserUseCase;

    /**
     * Constructor injection allowing decoupled initialization of the application layer.
     */
    public UserController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    /**
     * Endpoint to handle structural user registration.
     *
     * @param request Payload containing requested user account metadata.
     * @return ResponseEntity holding the generated User identity details and HTTP 21 Created state.
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        
        // 1. Convert structural request strings to domain strong-typed Enums
        Set<UserRole> domainRoles = request.roles().stream()
                .map(roleStr -> UserRole.valueOf(roleStr.toUpperCase()))
                .collect(Collectors.toSet());

        // 2. Map input DTO to the pure Domain Entity using the Manual Builder
        User userDomainInput = User.builder()
                .name(request.name())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .roles(domainRoles)
                .build();

        // 3. Trigger the isolated Application Use Case Execution
        User createdUser = createUserUseCase.execute(userDomainInput);

        // 4. Map resulting Domain Entity back into an infrastructure output DTO Response
        Set<String> responseRoles = createdUser.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toSet());

        UserResponse responseBody = new UserResponse(
                createdUser.getId(),
                createdUser.getName(),
                createdUser.getEmail(),
                createdUser.getPhoneNumber(),
                responseRoles
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }
}