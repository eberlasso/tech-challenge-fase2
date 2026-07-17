package com.postech.restaurantmanagement.infrastructure.controller;

import com.postech.restaurantmanagement.infrastructure.controller.api.RestaurantApi;
import com.postech.restaurantmanagement.infrastructure.controller.dto.CreateRestaurantRequest;
import com.postech.restaurantmanagement.infrastructure.controller.dto.RestaurantResponse;
import com.postech.restaurantmanagement.domain.entity.Restaurant;
import com.postech.restaurantmanagement.domain.usecase.CreateRestaurantUseCase;
import com.postech.restaurantmanagement.domain.usecase.DeleteRestaurantUseCase;
import com.postech.restaurantmanagement.domain.usecase.GetRestaurantByIdUseCase;
import com.postech.restaurantmanagement.domain.usecase.ListRestaurantsUseCase;
import com.postech.restaurantmanagement.domain.usecase.UpdateRestaurantUseCase;
import com.postech.restaurantmanagement.infrastructure.controller.mapper.RestaurantMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Interface Adapter REST Controller implementing the documented Restaurant API contract.
 */
@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController implements RestaurantApi {

    private final CreateRestaurantUseCase createRestaurantUseCase;
    private final ListRestaurantsUseCase listRestaurantsUseCase;
    private final GetRestaurantByIdUseCase getRestaurantByIdUseCase;
    private final UpdateRestaurantUseCase updateRestaurantUseCase;
    private final DeleteRestaurantUseCase deleteRestaurantUseCase;
    private final RestaurantMapper restaurantMapper;

    public RestaurantController(
            CreateRestaurantUseCase createRestaurantUseCase,
            ListRestaurantsUseCase listRestaurantsUseCase,
            GetRestaurantByIdUseCase getRestaurantByIdUseCase,
            UpdateRestaurantUseCase updateRestaurantUseCase,
            DeleteRestaurantUseCase deleteRestaurantUseCase,
            RestaurantMapper restaurantMapper) {
        this.createRestaurantUseCase = createRestaurantUseCase;
        this.listRestaurantsUseCase = listRestaurantsUseCase;
        this.getRestaurantByIdUseCase = getRestaurantByIdUseCase;
        this.updateRestaurantUseCase = updateRestaurantUseCase;
        this.deleteRestaurantUseCase = deleteRestaurantUseCase;
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

    @Override
    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> listRestaurants() {
        List<RestaurantResponse> response = listRestaurantsUseCase.execute().stream()
                .map(restaurantMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getRestaurantById(@PathVariable Long id) {
        Restaurant restaurant = getRestaurantByIdUseCase.execute(id);
        return ResponseEntity.ok(restaurantMapper.toResponse(restaurant));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> updateRestaurant(@PathVariable Long id, @RequestBody CreateRestaurantRequest request) {
        Restaurant restaurantDomainInput = restaurantMapper.toDomain(request);
        Restaurant updatedRestaurant = updateRestaurantUseCase.execute(withId(restaurantDomainInput, id));
        return ResponseEntity.ok(restaurantMapper.toResponse(updatedRestaurant));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        deleteRestaurantUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    private Restaurant withId(Restaurant restaurant, Long id) {
        return Restaurant.builder()
                .id(id)
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .cuisineType(restaurant.getCuisineType())
                .operatingHours(restaurant.getOperatingHours())
                .owner(restaurant.getOwner())
                .build();
    }
}