package com.postech.restaurantmanagement.infrastructure.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.postech.restaurantmanagement.infrastructure.controller.dto.CreateUserRequest;
import com.postech.restaurantmanagement.infrastructure.controller.dto.UserResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * OpenAPI documentation contract for User management operations.
 * Isolates documentation metadata from structural HTTP implementation code.
 */
@Tag(name = "Users", description = "Endpoints for managing system users and corporate classifications")
public interface UserApi {

    @Operation(
        summary = "Register a new user",
        description = "Validates core domain invariants and creates a system user associated with explicit roles.",
        responses = {
            @ApiResponse(
                responseCode = "201", 
                description = "User successfully created",
                content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @ApiResponse(
                responseCode = "400", 
                description = "Invalid payload structural validation constraints or broken invariants"
            ),
            @ApiResponse(
                responseCode = "409", 
                description = "Uniqueness constraint violation - Email address already registered"
            )
        }
    )
    ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request);

    @Operation(summary = "List users", description = "Returns all users registered in the system.")
    ResponseEntity<List<UserResponse>> listUsers();

    @Operation(summary = "Get user by ID", description = "Returns one user by identifier.")
    ResponseEntity<UserResponse> getUserById(@PathVariable Long id);

    @Operation(summary = "Update user", description = "Updates an existing user.")
    ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody CreateUserRequest request);

    @Operation(summary = "Delete user", description = "Removes a user by identifier.")
    ResponseEntity<Void> deleteUser(@PathVariable Long id);
}