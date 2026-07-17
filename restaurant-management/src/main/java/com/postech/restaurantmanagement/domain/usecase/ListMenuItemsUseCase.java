package com.postech.restaurantmanagement.domain.usecase;

import com.postech.restaurantmanagement.domain.entity.MenuItem;
import com.postech.restaurantmanagement.domain.gateway.MenuItemGateway;

import java.util.List;

public class ListMenuItemsUseCase {

    private final MenuItemGateway menuItemGateway;

    public ListMenuItemsUseCase(MenuItemGateway menuItemGateway) {
        this.menuItemGateway = menuItemGateway;
    }

    public List<MenuItem> execute(Long restaurantId) {
        if (restaurantId == null) {
            return menuItemGateway.findAll();
        }
        return menuItemGateway.findByRestaurantId(restaurantId);
    }
}
