package com.postech.restaurantmanagement.infrastructure.persistence;

import com.postech.restaurantmanagement.infrastructure.persistence.entity.MenuItemEntity;
import com.postech.restaurantmanagement.infrastructure.persistence.entity.RestaurantEntity;
import com.postech.restaurantmanagement.infrastructure.persistence.entity.UserEntity;
import com.postech.restaurantmanagement.infrastructure.persistence.repository.SpringDataMenuItemRepository;
import com.postech.restaurantmanagement.infrastructure.persistence.repository.SpringDataRestaurantRepository;
import com.postech.restaurantmanagement.infrastructure.persistence.repository.SpringDataUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class DatabaseH2IntegrationTest {

    @Autowired
    private SpringDataMenuItemRepository menuItemRepository;

    @Autowired
    private SpringDataRestaurantRepository restaurantRepository;

    @Autowired
    private SpringDataUserRepository userRepository;

    @BeforeEach
    void cleanup() {
        menuItemRepository.deleteAll();
        restaurantRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldPersistAndQueryRestaurantUniquenessByNameAndAddress() {
        UserEntity owner = userRepository.saveAndFlush(new UserEntity(
                null,
                "Owner",
                "owner-db-1@email.com",
                "pwd",
                "11999999999",
                1L
        ));

        RestaurantEntity restaurant = restaurantRepository.saveAndFlush(new RestaurantEntity(
                null,
                "Cantina",
                "Rua A, 100",
                "Italian",
                "10:00-22:00",
                owner
        ));

        assertNotNull(restaurant.getId());
        assertTrue(restaurantRepository.existsByNameIgnoreCaseAndAddressIgnoreCase("cantina", "rua a, 100"));
    }

    @Test
    void shouldRejectDuplicateRestaurantByUniqueIndex() {
        UserEntity owner = userRepository.saveAndFlush(new UserEntity(
                null,
                "Owner",
                "owner-db-2@email.com",
                "pwd",
                "11999999999",
                1L
        ));

        restaurantRepository.saveAndFlush(new RestaurantEntity(
                null,
                "Bistro",
                "Main St 1",
                "French",
                "10:00-22:00",
                owner
        ));

        assertThrows(DataIntegrityViolationException.class, () ->
                restaurantRepository.saveAndFlush(new RestaurantEntity(
                        null,
                        "Bistro",
                        "Main St 1",
                        "French",
                        "10:00-22:00",
                        owner
                ))
        );
    }

    @Test
    void shouldRejectDuplicateMenuItemNameInSameRestaurantButAllowInAnother() {
        UserEntity owner1 = userRepository.saveAndFlush(new UserEntity(
                null,
                "Owner1",
                "owner-db-3@email.com",
                "pwd",
                "11999999999",
                1L
        ));

        UserEntity owner2 = userRepository.saveAndFlush(new UserEntity(
                null,
                "Owner2",
                "owner-db-4@email.com",
                "pwd",
                "11888888888",
                1L
        ));

        RestaurantEntity restaurant1 = restaurantRepository.saveAndFlush(new RestaurantEntity(
                null,
                "Rest 1",
                "Address 1",
                "Italian",
                "10:00-22:00",
                owner1
        ));

        RestaurantEntity restaurant2 = restaurantRepository.saveAndFlush(new RestaurantEntity(
                null,
                "Rest 2",
                "Address 2",
                "Italian",
                "10:00-22:00",
                owner2
        ));

        menuItemRepository.saveAndFlush(new MenuItemEntity(
                null,
                "Burger",
                "Beef",
                new BigDecimal("30.00"),
                false,
                "/img/burger.jpg",
                restaurant1
        ));

        assertThrows(DataIntegrityViolationException.class, () ->
                menuItemRepository.saveAndFlush(new MenuItemEntity(
                        null,
                        "Burger",
                        "Duplicate same restaurant",
                        new BigDecimal("35.00"),
                        true,
                        "/img/burger2.jpg",
                        restaurant1
                ))
        );

        MenuItemEntity allowed = menuItemRepository.saveAndFlush(new MenuItemEntity(
                null,
                "Burger",
                "Same name different restaurant",
                new BigDecimal("32.00"),
                true,
                "/img/burger3.jpg",
                restaurant2
        ));

        assertNotNull(allowed.getId());
    }
}

