package com.postech.restaurantmanagement.domain.usecase;

import com.postech.restaurantmanagement.domain.entity.MenuItem;
import com.postech.restaurantmanagement.domain.exception.EntityValidationException;
import com.postech.restaurantmanagement.domain.exception.ResourceAlreadyExistsException;
import com.postech.restaurantmanagement.domain.gateway.MenuItemGateway;
import com.postech.restaurantmanagement.domain.gateway.RestaurantGateway;

/**
 * Use Case responsible for orchestrating the registration of a new Menu Item.
 * Enforces application constraints ensuring the targeted restaurant is valid and exists.
 */
public class CreateMenuItemUseCase {

    private final MenuItemGateway menuItemGateway;
    private final RestaurantGateway restaurantGateway;

    /**
     * Injects the boundary gateways required to validate and persist menu items.
     *
     * @param menuItemGateway   The contract for menu item data persistence.
     * @param restaurantGateway The contract for verifying restaurant existence.
     */
    public CreateMenuItemUseCase(MenuItemGateway menuItemGateway, RestaurantGateway restaurantGateway) {
        this.menuItemGateway = menuItemGateway;
        this.restaurantGateway = restaurantGateway;
    }

    /**
     * Executes validation and persistent storage rules for a new menu item.
     *
     * @param newMenuItem The structural entity instance provided by presentation boundary layers.
     * @return The fully saved MenuItem populated with system infrastructure identifiers.
     * @throws EntityValidationException if domain rules break or the target restaurant is unresolved.
     */
    public MenuItem execute(MenuItem newMenuItem) {
        // 1. Validates structural business properties of the incoming menu item
        if (newMenuItem == null || !newMenuItem.isValid()) {
            throw new EntityValidationException("Invalid menu item data. Verify name, description, price, and image path properties.");
        }

        // 2. Guarantees the restaurant exists before allowing item association
        restaurantGateway.findById(newMenuItem.getRestaurant().getId())
                .orElseThrow(() -> new EntityValidationException("The targeted restaurant for this menu item does not exist."));

        // 3. Enforces business rule: Menu item name must be unique within a restaurant
        if (menuItemGateway.existsByNameAndRestaurant(newMenuItem.getName(), newMenuItem.getRestaurant().getId())) {
            throw new ResourceAlreadyExistsException("A menu item with the name '" + newMenuItem.getName() + "' already exists in this restaurant.");
        }

        // 3. Persists the structural business item through the gateway boundary
        return menuItemGateway.save(newMenuItem);
    }
}