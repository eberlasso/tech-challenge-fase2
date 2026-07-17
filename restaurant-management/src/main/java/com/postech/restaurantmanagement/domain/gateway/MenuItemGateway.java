package com.postech.restaurantmanagement.domain.gateway;

import java.util.List;
import java.util.Optional;

import com.postech.restaurantmanagement.domain.entity.MenuItem;

/**
 * Boundary interface defining persistence capabilities for MenuItem entities.
 * Decouples core business requirements from structural framework implementations.
 */
public interface MenuItemGateway {

    MenuItem save(MenuItem menuItem);

    Optional<MenuItem> findById(Long id);

    List<MenuItem> findAll();

    List<MenuItem> findByRestaurantId(Long restaurantId);

    /**
     * Checks if a menu item with the same name already exists in a specific restaurant.
     *
     * @param name         The menu item name to check (case-insensitive).
     * @param restaurantId The restaurant identifier.
     * @return true if another menu item with the same name exists in this restaurant.
     */
    boolean existsByNameAndRestaurant(String name, Long restaurantId);

    boolean existsByNameAndRestaurantAndIdNot(String name, Long restaurantId, Long id);

    void deleteById(Long id);
}