package com.postech.restaurantmanagement.infrastructure.controller;


import com.postech.restaurantmanagement.infrastructure.controller.api.MenuItemApi;
import com.postech.restaurantmanagement.infrastructure.controller.dto.CreateMenuItemRequest;
import com.postech.restaurantmanagement.infrastructure.controller.dto.MenuItemResponse;
import com.postech.restaurantmanagement.domain.entity.MenuItem;
import com.postech.restaurantmanagement.domain.usecase.CreateMenuItemUseCase;
import com.postech.restaurantmanagement.infrastructure.controller.mapper.MenuItemMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Interface Adapter REST Controller implementing the documented Menu Item API contract.
 */
@RestController
@RequestMapping("/api/v1/menu-items")
public class MenuItemController implements MenuItemApi {

    private final CreateMenuItemUseCase createMenuItemUseCase;
    private final MenuItemMapper menuItemMapper;

    public MenuItemController(CreateMenuItemUseCase createMenuItemUseCase, MenuItemMapper menuItemMapper) {
        this.createMenuItemUseCase = createMenuItemUseCase;
        this.menuItemMapper = menuItemMapper;
    }

    @Override
    @PostMapping
    public ResponseEntity<MenuItemResponse> createMenuItem(@RequestBody CreateMenuItemRequest request) {

        MenuItem menuItemDomainInput = menuItemMapper.toDomain(request);

        MenuItem createdItem = createMenuItemUseCase.execute(menuItemDomainInput);

        MenuItemResponse responseBody = menuItemMapper.toResponse(createdItem);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }
}