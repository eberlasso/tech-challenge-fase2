package com.postech.restaurantmanagement.domain.usecase;

import com.postech.restaurantmanagement.domain.entity.Restaurant;
import com.postech.restaurantmanagement.domain.exception.ResourceNotFoundException;
import com.postech.restaurantmanagement.domain.gateway.AuditLogGateway;
import com.postech.restaurantmanagement.domain.gateway.RestaurantGateway;

public class DeleteRestaurantUseCase {

    private final RestaurantGateway restaurantGateway;
    private final AuditLogGateway auditLogGateway;

    public DeleteRestaurantUseCase(RestaurantGateway restaurantGateway, AuditLogGateway auditLogGateway) {
        this.restaurantGateway = restaurantGateway;
        this.auditLogGateway = auditLogGateway;
    }

    public void execute(Long id) {
        Restaurant restaurant = restaurantGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));

        restaurantGateway.deleteById(id);

        auditLogGateway.log(
                "RESTAURANT_DELETE",
                String.format("Restaurant '%s' (ID %d) removed.", restaurant.getName(), restaurant.getId()),
                "SYSTEM_USER"
        );
    }
}
