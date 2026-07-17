package com.postech.restaurantmanagement.infrastructure.persistence.repository;

import com.postech.restaurantmanagement.infrastructure.persistence.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Native Spring Data JPA Repository contract exposing relational queries for Restaurants.
 */
@Repository
public interface SpringDataRestaurantRepository extends JpaRepository<RestaurantEntity, Long> {

    // O Spring Data vai gerar automaticamente o SQL: SELECT * FROM tb_restaurants WHERE cuisine_type = ?
    List<RestaurantEntity> findByCuisineType(String cuisineType);

    boolean existsByNameIgnoreCaseAndAddressIgnoreCase(String name, String address);

    boolean existsByNameIgnoreCaseAndAddressIgnoreCaseAndIdNot(String name, String address, Long id);
}