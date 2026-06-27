package com.postech.restaurantmanagement.infrastructure.controller;

import com.postech.restaurantmanagement.infrastructure.controller.api.UserApi;
import com.postech.restaurantmanagement.infrastructure.controller.dto.CreateUserRequest;
import com.postech.restaurantmanagement.infrastructure.controller.dto.UserResponse;
import com.postech.restaurantmanagement.domain.entity.User;
import com.postech.restaurantmanagement.domain.usecase.CreateUserUseCase;
import com.postech.restaurantmanagement.infrastructure.controller.mapper.UserMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// ...existing code...

/**
 * Interface Adapter REST Controller exposing endpoints for User management.
 * Dispatches HTTP requests to core application business rules use cases.
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController implements UserApi{

    private final CreateUserUseCase createUserUseCase;
    private final UserMapper userMapper;

    /**
     * Constructor injection allowing decoupled initialization of the application layer.
     */
    public UserController(CreateUserUseCase createUserUseCase, UserMapper userMapper) {
        this.createUserUseCase = createUserUseCase;
        this.userMapper = userMapper;
    }

    /**
     * Endpoint to handle structural user registration.
     *
     * @param request Payload containing requested user account metadata.
     * @return ResponseEntity holding the generated User identity details and HTTP 21 Created state.
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        
        // Map request -> domain
        User userDomainInput = userMapper.toDomain(request);

        // Execute use case
        User createdUser = createUserUseCase.execute(userDomainInput);

        // Map domain -> response
        UserResponse responseBody = userMapper.toResponse(createdUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }
}