package com.postech.restaurantmanagement.domain.usecase;

import com.postech.restaurantmanagement.domain.entity.Restaurant;
import com.postech.restaurantmanagement.domain.exception.ResourceNotFoundException;
import com.postech.restaurantmanagement.domain.gateway.RestaurantGateway;

public class GetRestaurantByIdUseCase {

    private final RestaurantGateway restaurantGateway;

    public GetRestaurantByIdUseCase(RestaurantGateway restaurantGateway) {
        this.restaurantGateway = restaurantGateway;
    }

    public Restaurant execute(Long id) {
        return restaurantGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
    }
}
