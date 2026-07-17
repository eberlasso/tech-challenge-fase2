package com.postech.restaurantmanagement.domain.gateway;

import java.util.List;
import java.util.Optional;

import com.postech.restaurantmanagement.domain.entity.User;

public interface UserGateway {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    boolean existsByEmailAndIdNot(String email, Long id);
    void deleteById(Long id);
}