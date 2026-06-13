package com.postech.restaurantmanagement.infrastructure.controller;


import com.postech.restaurantmanagement.infrastructure.controller.api.MenuItemApi;
import com.postech.restaurantmanagement.infrastructure.controller.dto.CreateMenuItemRequest;
import com.postech.restaurantmanagement.infrastructure.controller.dto.MenuItemResponse;
import com.postech.restaurantmanagement.domain.entity.MenuItem;
import com.postech.restaurantmanagement.domain.entity.Restaurant;
import com.postech.restaurantmanagement.domain.usecase.CreateMenuItemUseCase;
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

    public MenuItemController(CreateMenuItemUseCase createMenuItemUseCase) {
        this.createMenuItemUseCase = createMenuItemUseCase;
    }

    @Override
    @PostMapping
    public ResponseEntity<MenuItemResponse> createMenuItem(@RequestBody CreateMenuItemRequest request) {

        Restaurant restaurantProxy = Restaurant.builder()
                .id(request.restaurantId())
                .build();

        MenuItem menuItemDomainInput = MenuItem.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .availableOnlyInRestaurant(request.availableOnlyInRestaurant())
                .imagePath(request.imagePath())
                .restaurant(restaurantProxy)
                .build();

        MenuItem createdItem = createMenuItemUseCase.execute(menuItemDomainInput);

        MenuItemResponse responseBody = new MenuItemResponse(
                createdItem.getId(),
                createdItem.getName(),
                createdItem.getDescription(),
                createdItem.getPrice(),
                createdItem.isAvailableOnlyInRestaurant(),
                createdItem.getImagePath(),
                createdItem.getRestaurant().getId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }
}