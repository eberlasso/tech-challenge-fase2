package com.postech.restaurantmanagement.infrastructure.persistence.repository;

import com.postech.restaurantmanagement.infrastructure.persistence.entity.MenuItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Native Spring Data JPA Repository contract exposing relational queries for Menu Items.
 */
@Repository
public interface SpringDataMenuItemRepository extends JpaRepository<MenuItemEntity, Long> {
    // O Spring Data vai gerar automaticamente o SQL filtrando pela FK do restaurante
    List<MenuItemEntity> findByRestaurantId(Long restaurantId);

    boolean existsByNameIgnoreCaseAndRestaurantId(String name, Long restaurantId);

    boolean existsByNameIgnoreCaseAndRestaurantIdAndIdNot(String name, Long restaurantId, Long id);
}