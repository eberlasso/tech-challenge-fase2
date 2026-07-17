package com.postech.restaurantmanagement.domain.usecase;

import com.postech.restaurantmanagement.domain.entity.Restaurant;
import com.postech.restaurantmanagement.domain.exception.EntityValidationException;
import com.postech.restaurantmanagement.domain.exception.ResourceAlreadyExistsException;
import com.postech.restaurantmanagement.domain.exception.ResourceNotFoundException;
import com.postech.restaurantmanagement.domain.gateway.AuditLogGateway;
import com.postech.restaurantmanagement.domain.gateway.RestaurantGateway;

public class UpdateRestaurantUseCase {

    private final RestaurantGateway restaurantGateway;
    private final AuditLogGateway auditLogGateway;

    public UpdateRestaurantUseCase(RestaurantGateway restaurantGateway, AuditLogGateway auditLogGateway) {
        this.restaurantGateway = restaurantGateway;
        this.auditLogGateway = auditLogGateway;
    }

    public Restaurant execute(Restaurant restaurantToUpdate) {
        if (restaurantToUpdate == null || restaurantToUpdate.getId() == null || !restaurantToUpdate.isValid()) {
            throw new EntityValidationException("Invalid restaurant data. Verify required fields and owner reference.");
        }

        restaurantGateway.findById(restaurantToUpdate.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantToUpdate.getId()));

        if (restaurantGateway.existsByNameAndAddressAndIdNot(
                restaurantToUpdate.getName(),
                restaurantToUpdate.getAddress(),
                restaurantToUpdate.getId())) {
            throw new ResourceAlreadyExistsException("A restaurant with the same name and address already exists.");
        }

        Restaurant updatedRestaurant = restaurantGateway.save(restaurantToUpdate);

        auditLogGateway.log(
                "RESTAURANT_UPDATE",
                String.format("Restaurant '%s' (ID %d) updated.", updatedRestaurant.getName(), updatedRestaurant.getId()),
                "SYSTEM_USER"
        );

        return updatedRestaurant;
    }
}
