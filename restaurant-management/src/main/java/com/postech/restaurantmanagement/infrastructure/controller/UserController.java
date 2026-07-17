package com.postech.restaurantmanagement.infrastructure.controller;

import com.postech.restaurantmanagement.infrastructure.controller.api.UserApi;
import com.postech.restaurantmanagement.infrastructure.controller.dto.CreateUserRequest;
import com.postech.restaurantmanagement.infrastructure.controller.dto.UserResponse;
import com.postech.restaurantmanagement.domain.entity.User;
import com.postech.restaurantmanagement.domain.usecase.CreateUserUseCase;
import com.postech.restaurantmanagement.domain.usecase.DeleteUserUseCase;
import com.postech.restaurantmanagement.domain.usecase.GetUserByIdUseCase;
import com.postech.restaurantmanagement.domain.usecase.ListUsersUseCase;
import com.postech.restaurantmanagement.domain.usecase.UpdateUserUseCase;
import com.postech.restaurantmanagement.infrastructure.controller.mapper.UserMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// ...existing code...

/**
 * Interface Adapter REST Controller exposing endpoints for User management.
 * Dispatches HTTP requests to core application business rules use cases.
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserController implements UserApi{

    private final CreateUserUseCase createUserUseCase;
    private final ListUsersUseCase listUsersUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final UserMapper userMapper;

    /**
     * Constructor injection allowing decoupled initialization of the application layer.
     */
    public UserController(
            CreateUserUseCase createUserUseCase,
            ListUsersUseCase listUsersUseCase,
            GetUserByIdUseCase getUserByIdUseCase,
            UpdateUserUseCase updateUserUseCase,
            DeleteUserUseCase deleteUserUseCase,
            UserMapper userMapper) {
        this.createUserUseCase = createUserUseCase;
        this.listUsersUseCase = listUsersUseCase;
        this.getUserByIdUseCase = getUserByIdUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
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

    @Override
    @GetMapping
    public ResponseEntity<List<UserResponse>> listUsers() {
        List<UserResponse> response = listUsersUseCase.execute().stream()
                .map(userMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        User user = getUserByIdUseCase.execute(id);
        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody CreateUserRequest request) {
        User domainInput = userMapper.toDomain(request);
        User updatedUser = updateUserUseCase.execute(withId(domainInput, id));
        return ResponseEntity.ok(userMapper.toResponse(updatedUser));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        deleteUserUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    private User withId(User user, Long id) {
        return User.builder()
                .id(id)
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .roles(user.getRoles())
                .build();
    }
}