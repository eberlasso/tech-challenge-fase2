package com.postech.restaurantmanagement.infrastructure.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.postech.restaurantmanagement.infrastructure.controller.dto.CreateMenuItemRequest;
import com.postech.restaurantmanagement.infrastructure.controller.dto.MenuItemResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

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

    @Operation(summary = "List menu items", description = "Returns all menu items, optionally filtered by restaurant.")
    ResponseEntity<List<MenuItemResponse>> listMenuItems(@RequestParam(required = false) Long restaurantId);

    @Operation(summary = "Get menu item by ID", description = "Returns one menu item by identifier.")
    ResponseEntity<MenuItemResponse> getMenuItemById(@PathVariable Long id);

    @Operation(summary = "Update menu item", description = "Updates an existing menu item.")
    ResponseEntity<MenuItemResponse> updateMenuItem(@PathVariable Long id, @RequestBody CreateMenuItemRequest request);

    @Operation(summary = "Delete menu item", description = "Removes a menu item by identifier.")
    ResponseEntity<Void> deleteMenuItem(@PathVariable Long id);
}