package com.postech.restaurantmanagement.domain.usecase;

import com.postech.restaurantmanagement.domain.entity.MenuItem;
import com.postech.restaurantmanagement.domain.exception.ResourceNotFoundException;
import com.postech.restaurantmanagement.domain.gateway.AuditLogGateway;
import com.postech.restaurantmanagement.domain.gateway.MenuItemGateway;

public class DeleteMenuItemUseCase {

    private final MenuItemGateway menuItemGateway;
    private final AuditLogGateway auditLogGateway;

    public DeleteMenuItemUseCase(MenuItemGateway menuItemGateway, AuditLogGateway auditLogGateway) {
        this.menuItemGateway = menuItemGateway;
        this.auditLogGateway = auditLogGateway;
    }

    public void execute(Long id) {
        MenuItem menuItem = menuItemGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found with id: " + id));

        menuItemGateway.deleteById(id);

        auditLogGateway.log(
                "MENU_ITEM_DELETE",
                String.format("Menu item '%s' (ID %d) removed.", menuItem.getName(), menuItem.getId()),
                "SYSTEM_USER"
        );
    }
}
