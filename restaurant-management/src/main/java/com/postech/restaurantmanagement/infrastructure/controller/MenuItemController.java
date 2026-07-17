package com.postech.restaurantmanagement.infrastructure.controller;


import com.postech.restaurantmanagement.infrastructure.controller.api.MenuItemApi;
import com.postech.restaurantmanagement.infrastructure.controller.dto.CreateMenuItemRequest;
import com.postech.restaurantmanagement.infrastructure.controller.dto.MenuItemResponse;
import com.postech.restaurantmanagement.domain.entity.MenuItem;
import com.postech.restaurantmanagement.domain.usecase.CreateMenuItemUseCase;
import com.postech.restaurantmanagement.domain.usecase.DeleteMenuItemUseCase;
import com.postech.restaurantmanagement.domain.usecase.GetMenuItemByIdUseCase;
import com.postech.restaurantmanagement.domain.usecase.ListMenuItemsUseCase;
import com.postech.restaurantmanagement.domain.usecase.UpdateMenuItemUseCase;
import com.postech.restaurantmanagement.infrastructure.controller.mapper.MenuItemMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Interface Adapter REST Controller implementing the documented Menu Item API contract.
 */
@RestController
@RequestMapping("/api/v1/menu-items")
public class MenuItemController implements MenuItemApi {

    private final CreateMenuItemUseCase createMenuItemUseCase;
    private final ListMenuItemsUseCase listMenuItemsUseCase;
    private final GetMenuItemByIdUseCase getMenuItemByIdUseCase;
    private final UpdateMenuItemUseCase updateMenuItemUseCase;
    private final DeleteMenuItemUseCase deleteMenuItemUseCase;
    private final MenuItemMapper menuItemMapper;

    public MenuItemController(
            CreateMenuItemUseCase createMenuItemUseCase,
            ListMenuItemsUseCase listMenuItemsUseCase,
            GetMenuItemByIdUseCase getMenuItemByIdUseCase,
            UpdateMenuItemUseCase updateMenuItemUseCase,
            DeleteMenuItemUseCase deleteMenuItemUseCase,
            MenuItemMapper menuItemMapper) {
        this.createMenuItemUseCase = createMenuItemUseCase;
        this.listMenuItemsUseCase = listMenuItemsUseCase;
        this.getMenuItemByIdUseCase = getMenuItemByIdUseCase;
        this.updateMenuItemUseCase = updateMenuItemUseCase;
        this.deleteMenuItemUseCase = deleteMenuItemUseCase;
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

    @Override
    @GetMapping
    public ResponseEntity<List<MenuItemResponse>> listMenuItems(@RequestParam(required = false) Long restaurantId) {
        List<MenuItemResponse> response = listMenuItemsUseCase.execute(restaurantId).stream()
                .map(menuItemMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponse> getMenuItemById(@PathVariable Long id) {
        MenuItem menuItem = getMenuItemByIdUseCase.execute(id);
        return ResponseEntity.ok(menuItemMapper.toResponse(menuItem));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<MenuItemResponse> updateMenuItem(@PathVariable Long id, @RequestBody CreateMenuItemRequest request) {
        MenuItem menuItemDomainInput = menuItemMapper.toDomain(request);
        MenuItem updatedItem = updateMenuItemUseCase.execute(withId(menuItemDomainInput, id));
        return ResponseEntity.ok(menuItemMapper.toResponse(updatedItem));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        deleteMenuItemUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    private MenuItem withId(MenuItem menuItem, Long id) {
        return MenuItem.builder()
                .id(id)
                .name(menuItem.getName())
                .description(menuItem.getDescription())
                .price(menuItem.getPrice())
                .availableOnlyInRestaurant(menuItem.isAvailableOnlyInRestaurant())
                .imagePath(menuItem.getImagePath())
                .restaurant(menuItem.getRestaurant())
                .build();
    }
}