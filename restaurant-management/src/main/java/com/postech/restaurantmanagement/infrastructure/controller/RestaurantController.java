package com.postech.restaurantmanagement.infrastructure.controller;

import com.postech.restaurantmanagement.infrastructure.controller.api.RestaurantApi;
import com.postech.restaurantmanagement.infrastructure.controller.dto.CreateRestaurantRequest;
import com.postech.restaurantmanagement.infrastructure.controller.dto.RestaurantResponse;
import com.postech.restaurantmanagement.domain.entity.Restaurant;
import com.postech.restaurantmanagement.domain.usecase.CreateRestaurantUseCase;
import com.postech.restaurantmanagement.infrastructure.controller.mapper.RestaurantMapper;
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
    private final RestaurantMapper restaurantMapper;

    public RestaurantController(CreateRestaurantUseCase createRestaurantUseCase, RestaurantMapper restaurantMapper) {
        this.createRestaurantUseCase = createRestaurantUseCase;
        this.restaurantMapper = restaurantMapper;
    }

    @Override
    @PostMapping
    public ResponseEntity<RestaurantResponse> createRestaurant(@RequestBody CreateRestaurantRequest request) {

        Restaurant restaurantDomainInput = restaurantMapper.toDomain(request);

        Restaurant createdRestaurant = createRestaurantUseCase.execute(restaurantDomainInput);

        RestaurantResponse responseBody = restaurantMapper.toResponse(createdRestaurant);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }
}