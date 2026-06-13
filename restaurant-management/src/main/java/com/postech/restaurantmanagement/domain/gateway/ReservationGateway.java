package com.postech.restaurantmanagement.domain.gateway;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.postech.restaurantmanagement.domain.entity.Reservation;

/**
 * Boundary interface defining persistence capabilities for Reservation entities.
 * Decouples core business requirements from infrastructure framework implementations.
 */
public interface ReservationGateway {
    
    /**
     * Persists a new reservation record or updates an existing tracking instance.
     *
     * @param reservation The pure business record to save.
     * @return The updated entity instance containing system generated tracking identifiers.
     */
    Reservation save(Reservation reservation);
    
    /**
     * Locates a specific tracking record based on its unique database indicator.
     *
     * @param id The reference key lookup variable.
     * @return An Optional wrapper including the match, or empty if unresolved.
     */
    Optional<Reservation> findById(Long id);
    
    /**
     * Retrieves all reservations registered under a specific restaurant at a targeted timeline schedule.
     * Used primarily for allocation capacity checks.
     *
     * @param restaurantId The unique identifier of the target restaurant.
     * @param dateTime The targeted scheduling timeline boundary.
     * @return A list containing matching registered records.
     */
    List<Reservation> findByRestaurantIdAndDateTime(Long restaurantId, LocalDateTime dateTime);
    
    /**
     * Gathers all reservation history linked to a unique client identifier.
     *
     * @param clientId The specific user tracking indicator.
     * @return A list of records associated with the client.
     */
    List<Reservation> findByClientId(Long clientId);
}