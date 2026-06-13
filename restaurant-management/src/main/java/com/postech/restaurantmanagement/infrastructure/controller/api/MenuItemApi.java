package com.postech.restaurantmanagement.infrastructure.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.postech.restaurantmanagement.infrastructure.controller.dto.CreateMenuItemRequest;
import com.postech.restaurantmanagement.infrastructure.controller.dto.MenuItemResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * OpenAPI documentation contract for Menu Item catalog operations.
 */
@Tag(name = "Menu Items", description = "Endpoints for managing restaurant menu catalog entries and local availability constraints")
public interface MenuItemApi {

    @Operation(
        summary = "Create a new menu item",
        description = "Validates structural catalogs data, positive pricing structures, and binds the item to a valid active restaurant.",
        responses = {
            @ApiResponse(
                responseCode = "201", 
                description = "Menu item successfully created",
                content = @Content(schema = @Schema(implementation = MenuItemResponse.class))
            ),
            @ApiResponse(
                responseCode = "400", 
                description = "Invalid product structure metrics, illegal zero pricing, or non-existent restaurant attachment"
            )
        }
    )
    ResponseEntity<MenuItemResponse> createMenuItem(@RequestBody CreateMenuItemRequest request);
}