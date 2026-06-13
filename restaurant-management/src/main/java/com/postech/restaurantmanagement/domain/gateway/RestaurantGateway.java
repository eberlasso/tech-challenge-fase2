package com.postech.restaurantmanagement.domain.gateway;

import java.util.List;
import java.util.Optional;

import com.postech.restaurantmanagement.domain.entity.Restaurant;

/**
 * Boundary interface defining persistence capabilities for Restaurant entities.
 * Decouples core business requirements from structural framework implementations.
 */
public interface RestaurantGateway {

    /**
     * Persists a new restaurant record or updates an existing infrastructure instance.
     *
     * @param restaurant The pure business domain record to save.
     * @return The updated entity instance containing system generated tracking identifiers.
     */
    Restaurant save(Restaurant restaurant);

    /**
     * Locates a specific restaurant record based on its unique database indicator.
     *
     * @param id The reference key lookup variable.
     * @return An Optional wrapper including the match, or empty if unresolved.
     */
    Optional<Restaurant> findById(Long id);

    /**
     * Retrieves all registered restaurants in the system.
     *
     * @return A list containing all active restaurant records.
     */
    List<Restaurant> findAll();

    /**
     * Searches for restaurants matching a specific culinary type or cuisine category.
     *
     * @param cuisineType The text string representing the target cuisine (e.g., Italian, Brazilian).
     * @return A list of matching restaurant records.
     */
    List<Restaurant> findByCuisineType(String cuisineType);
}