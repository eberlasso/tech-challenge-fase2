package com.postech.restaurantmanagement.domain.gateway;

import java.util.Optional;

import com.postech.restaurantmanagement.domain.entity.Client;

/**
 * Boundary interface defining persistence capabilities for Client entities.
 * Decouples core business requirements from structural framework implementations.
 */
public interface IClientGateway {
    /**
     * Registers a new client or updates an existing identity record.
     *
     * @param client The pure business record to save.
     * @return The updated entity filled with infrastructure tracking identifiers.
     */
    Client save(Client client);

    /**
     * Locates a registered client tracking record based on their unique database key.
     *
     * @param id The reference key lookup variable.
     * @return An Optional wrapper including the match, or empty if unresolved.
     */
    Optional<Client> findById(Long id);

    /**
     * Searches for an existing client using their unique email address registry string.
     *
     * @param email The exact target email format.
     * @return An Optional instance carrying the matched entity record.
     */
    Optional<Client> findByEmail(String email);

}
