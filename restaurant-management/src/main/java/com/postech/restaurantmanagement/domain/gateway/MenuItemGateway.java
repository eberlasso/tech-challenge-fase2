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

    List<List<MenuItem>> findByRestaurantId(Long restaurantId);
}