package com.postech.restaurantmanagement.domain.usecase;

import com.postech.restaurantmanagement.domain.entity.MenuItem;
import com.postech.restaurantmanagement.domain.exception.ResourceNotFoundException;
import com.postech.restaurantmanagement.domain.gateway.MenuItemGateway;

public class GetMenuItemByIdUseCase {

    private final MenuItemGateway menuItemGateway;

    public GetMenuItemByIdUseCase(MenuItemGateway menuItemGateway) {
        this.menuItemGateway = menuItemGateway;
    }

    public MenuItem execute(Long id) {
        return menuItemGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));
    }
}
