package com.postech.restaurantmanagement.domain.entity;

import java.math.BigDecimal;

/**
 * Enterprise Business Entity representing a Menu Item sold by a restaurant.
 * Governs core rules regarding pricing, availability constraints, and relational integrity.
 */
public class MenuItem {

    private final Long id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final boolean availableOnlyInRestaurant;
    private final String imagePath; // Stores the mock physical path string as requested in specifications
    private final Restaurant restaurant; // Relational anchor to the owning restaurant establishment

    private MenuItem(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.price = builder.price;
        this.availableOnlyInRestaurant = builder.availableOnlyInRestaurant;
        this.imagePath = builder.imagePath;
        this.restaurant = builder.restaurant;
    }

    // --- CORE BUSINESS RULES ---

    /**
     * Validates if the menu item enterprise invariants are fully satisfied.
     * Requires valid descriptive metadata, a strictly positive non-null price,
     * an image tracking reference path, and an associated restaurant reference.
     * Note: Restaurant validation (existence, completeness) is handled by the use case layer.
     *
     * @return true if all corporate business rules are valid, false otherwise.
     */
    public boolean isValid() {
        return name != null && !name.isBlank() &&
               description != null && !description.isBlank() &&
               price != null && price.compareTo(BigDecimal.ZERO) > 0 &&
               imagePath != null && !imagePath.isBlank() &&
               restaurant != null && restaurant.getId() != null;
    }

    // --- PURE GETTERS ---

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public boolean isAvailableOnlyInRestaurant() { return availableOnlyInRestaurant; }
    public String getImagePath() { return imagePath; }
    public Restaurant getRestaurant() { return restaurant; }

    // --- MANUAL BUILDER PATTERN ---

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private boolean availableOnlyInRestaurant;
        private String imagePath;
        private Restaurant restaurant;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder availableOnlyInRestaurant(boolean availableOnlyInRestaurant) {
            this.availableOnlyInRestaurant = availableOnlyInRestaurant;
            return this;
        }

        public Builder imagePath(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public Builder restaurant(Restaurant restaurant) {
            this.restaurant = restaurant;
            return this;
        }

        public MenuItem build() {
            return new MenuItem(this);
        }
    }
}