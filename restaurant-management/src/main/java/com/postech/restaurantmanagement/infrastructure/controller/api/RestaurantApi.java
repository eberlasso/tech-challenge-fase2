package com.postech.restaurantmanagement.infrastructure.controller.api;

import com.postech.restaurantmanagement.infrastructure.controller.dto.CreateRestaurantRequest;
import com.postech.restaurantmanagement.infrastructure.controller.dto.RestaurantResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * OpenAPI documentation contract for Restaurant management operations.
 * Isolates documentation metadata from structural HTTP implementation code.
 */
@Tag(name = "Restaurants", description = "Endpoints for managing restaurant registrations and structural ownership metadata")
public interface RestaurantApi {

    @Operation(
        summary = "Register a new restaurant",
        description = "Validates core restaurant invariants and binds the establishment to an existing user with RESTAURANT_OWNER privileges.",
        responses = {
            @ApiResponse(
                responseCode = "201", 
                description = "Restaurant successfully registered",
                content = @Content(schema = @Schema(implementation = RestaurantResponse.class))
            ),
            @ApiResponse(
                responseCode = "400", 
                description = "Invalid payload structural data or broken business invariants"
            )
        }
    )
    ResponseEntity<RestaurantResponse> createRestaurant(@RequestBody CreateRestaurantRequest request);

    @Operation(summary = "List restaurants", description = "Returns all restaurants.")
    ResponseEntity<List<RestaurantResponse>> listRestaurants();

    @Operation(summary = "Get restaurant by ID", description = "Returns one restaurant by identifier.")
    ResponseEntity<RestaurantResponse> getRestaurantById(@PathVariable Long id);

    @Operation(summary = "Update restaurant", description = "Updates an existing restaurant.")
    ResponseEntity<RestaurantResponse> updateRestaurant(@PathVariable Long id, @RequestBody CreateRestaurantRequest request);

    @Operation(summary = "Delete restaurant", description = "Removes a restaurant by identifier.")
    ResponseEntity<Void> deleteRestaurant(@PathVariable Long id);
}