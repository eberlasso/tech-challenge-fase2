package com.postech.restaurantmanagement.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Structural JPA Entity mapping the restaurant establishment records to the PostgreSQL database.
 */
@Entity
@Table(name = "tb_restaurants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(name = "cuisine_type", nullable = false, length = 100)
    private String cuisineType;

    @Column(name = "operating_hours", nullable = false, length = 100)
    private String operatingHours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;
}