package com.postech.restaurantmanagement.domain.usecase;

import com.postech.restaurantmanagement.domain.entity.Restaurant;
import com.postech.restaurantmanagement.domain.entity.User;
import com.postech.restaurantmanagement.domain.exception.ResourceAlreadyExistsException;
import com.postech.restaurantmanagement.domain.exception.ResourceNotFoundException;
import com.postech.restaurantmanagement.domain.gateway.AuditLogGateway;
import com.postech.restaurantmanagement.domain.gateway.RestaurantGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantCrudUseCasesTest {

    @Mock private RestaurantGateway restaurantGateway;
    @Mock private AuditLogGateway auditLogGateway;

    private Restaurant restaurant() {
        return Restaurant.builder()
                .id(1L)
                .name("Bistro")
                .address("Main")
                .cuisineType("French")
                .operatingHours("10-22")
                .owner(User.builder().id(7L).build())
                .build();
    }

    @Test
    void shouldListRestaurants() {
        when(restaurantGateway.findAll()).thenReturn(List.of(restaurant()));
        assertEquals(1, new ListRestaurantsUseCase(restaurantGateway).execute().size());
    }

    @Test
    void shouldGetRestaurantById() {
        when(restaurantGateway.findById(1L)).thenReturn(Optional.of(restaurant()));
        assertEquals(1L, new GetRestaurantByIdUseCase(restaurantGateway).execute(1L).getId());
    }

    @Test
    void shouldThrowWhenRestaurantNotFound() {
        when(restaurantGateway.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> new GetRestaurantByIdUseCase(restaurantGateway).execute(1L));
    }

    @Test
    void shouldUpdateRestaurant() {
        Restaurant restaurant = restaurant();
        when(restaurantGateway.findById(1L)).thenReturn(Optional.of(restaurant));
        when(restaurantGateway.existsByNameAndAddressAndIdNot("Bistro", "Main", 1L)).thenReturn(false);
        when(restaurantGateway.save(restaurant)).thenReturn(restaurant);

        Restaurant result = new UpdateRestaurantUseCase(restaurantGateway, auditLogGateway).execute(restaurant);

        assertEquals(1L, result.getId());
        verify(auditLogGateway).log(eq("RESTAURANT_UPDATE"), contains("updated"), eq("SYSTEM_USER"));
    }

    @Test
    void shouldThrowWhenUpdatingRestaurantDuplicateIdentity() {
        Restaurant restaurant = restaurant();
        when(restaurantGateway.findById(1L)).thenReturn(Optional.of(restaurant));
        when(restaurantGateway.existsByNameAndAddressAndIdNot("Bistro", "Main", 1L)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> new UpdateRestaurantUseCase(restaurantGateway, auditLogGateway).execute(restaurant));
    }

    @Test
    void shouldDeleteRestaurant() {
        Restaurant restaurant = restaurant();
        when(restaurantGateway.findById(1L)).thenReturn(Optional.of(restaurant));

        new DeleteRestaurantUseCase(restaurantGateway, auditLogGateway).execute(1L);

        verify(restaurantGateway).deleteById(1L);
        verify(auditLogGateway).log(eq("RESTAURANT_DELETE"), contains("removed"), eq("SYSTEM_USER"));
    }
}
