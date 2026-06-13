package com.postech.restaurantmanagement.infrastructure.controller;

import com.postech.restaurantmanagement.infrastructure.controller.api.RestaurantApi;
import com.postech.restaurantmanagement.infrastructure.controller.dto.CreateRestaurantRequest;
import com.postech.restaurantmanagement.infrastructure.controller.dto.RestaurantResponse;
import com.postech.restaurantmanagement.domain.entity.Restaurant;
import com.postech.restaurantmanagement.domain.entity.User;
import com.postech.restaurantmanagement.domain.usecase.CreateRestaurantUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Interface Adapter REST Controller implementing the documented Restaurant API contract.
 */
@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController implements RestaurantApi {

    private final CreateRestaurantUseCase createRestaurantUseCase;

    public RestaurantController(CreateRestaurantUseCase createRestaurantUseCase) {
        this.createRestaurantUseCase = createRestaurantUseCase;
    }

    @Override
    @PostMapping
    public ResponseEntity<RestaurantResponse> createRestaurant(@RequestBody CreateRestaurantRequest request) {

        User ownerProxy = User.builder()
                .id(request.ownerId())
                .build();

        Restaurant restaurantDomainInput = Restaurant.builder()
                .name(request.name())
                .address(request.address())
                .cuisineType(request.cuisineType())
                .operatingHours(request.operatingHours())
                .owner(ownerProxy)
                .build();

        Restaurant createdRestaurant = createRestaurantUseCase.execute(restaurantDomainInput);

        RestaurantResponse responseBody = new RestaurantResponse(
                createdRestaurant.getId(),
                createdRestaurant.getName(),
                createdRestaurant.getAddress(),
                createdRestaurant.getCuisineType(),
                createdRestaurant.getOperatingHours(),
                createdRestaurant.getOwner().getId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }
}