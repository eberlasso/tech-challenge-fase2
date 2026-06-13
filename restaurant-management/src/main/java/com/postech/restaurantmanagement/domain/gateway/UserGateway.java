package com.postech.restaurantmanagement.domain.gateway;

import java.util.Optional;

import com.postech.restaurantmanagement.domain.entity.User;

public interface UserGateway {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
}