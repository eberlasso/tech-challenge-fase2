package com.postech.restaurantmanagement.domain.usecase;

import com.postech.restaurantmanagement.domain.entity.MenuItem;
import com.postech.restaurantmanagement.domain.exception.EntityValidationException;
import com.postech.restaurantmanagement.domain.exception.ResourceAlreadyExistsException;
import com.postech.restaurantmanagement.domain.exception.ResourceNotFoundException;
import com.postech.restaurantmanagement.domain.gateway.AuditLogGateway;
import com.postech.restaurantmanagement.domain.gateway.MenuItemGateway;
import com.postech.restaurantmanagement.domain.gateway.RestaurantGateway;

public class UpdateMenuItemUseCase {

    private final MenuItemGateway menuItemGateway;
    private final RestaurantGateway restaurantGateway;
    private final AuditLogGateway auditLogGateway;

    public UpdateMenuItemUseCase(
            MenuItemGateway menuItemGateway,
            RestaurantGateway restaurantGateway,
            AuditLogGateway auditLogGateway) {
        this.menuItemGateway = menuItemGateway;
        this.restaurantGateway = restaurantGateway;
        this.auditLogGateway = auditLogGateway;
    }

    public MenuItem execute(MenuItem menuItemToUpdate) {
        if (menuItemToUpdate == null || menuItemToUpdate.getId() == null || !menuItemToUpdate.isValid()) {
            throw new EntityValidationException("Invalid menu item data. Verify name, description, price, and image path properties.");
        }

        menuItemGateway.findById(menuItemToUpdate.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + menuItemToUpdate.getId()));

        restaurantGateway.findById(menuItemToUpdate.getRestaurant().getId())
                .orElseThrow(() -> new EntityValidationException("The targeted restaurant for this menu item does not exist."));

        if (menuItemGateway.existsByNameAndRestaurantAndIdNot(
                menuItemToUpdate.getName(),
                menuItemToUpdate.getRestaurant().getId(),
                menuItemToUpdate.getId())) {
            throw new ResourceAlreadyExistsException(
                    "A menu item with the name '" + menuItemToUpdate.getName() + "' already exists in this restaurant.");
        }

        MenuItem updatedItem = menuItemGateway.save(menuItemToUpdate);

        auditLogGateway.log(
                "MENU_ITEM_UPDATE",
                String.format("Menu item '%s' (ID %d) updated.", updatedItem.getName(), updatedItem.getId()),
                "SYSTEM_USER"
        );

        return updatedItem;
    }
}
