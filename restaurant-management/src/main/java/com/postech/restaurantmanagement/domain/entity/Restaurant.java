package com.postech.restaurantmanagement.domain.entity;

/**
 * Enterprise Business Entity representing a Restaurant within the platform.
 * Governs core corporate business rules regarding establishment registry,
 * cuisine classifications, and operational ownership constraints.
 */
public class Restaurant {

    private final Long id;
    private final String name;
    private final String address;
    private final String cuisineType;
    private final String operatingHours;
    private final User owner; // Tied strictly to an existing user responsible for this establishment

    // Private constructor to enforce validation constraints via the manual Builder
    private Restaurant(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.address = builder.address;
        this.cuisineType = builder.cuisineType;
        this.operatingHours = builder.operatingHours;
        this.owner = builder.owner;
    }

    // --- CORE BUSINESS RULES ---

    public boolean isValid() {
        return name != null && !name.isBlank() &&
               address != null && !address.isBlank() &&
               cuisineType != null && !cuisineType.isBlank() &&
               operatingHours != null && !operatingHours.isBlank() &&
               owner != null && owner.getId() != null;
    }

    // --- PURE GETTERS ---

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getCuisineType() { return cuisineType; }
    public String getOperatingHours() { return operatingHours; }
    public User getOwner() { return owner; }

    // --- MANUAL BUILDER PATTERN ---

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String name;
        private String address;
        private String cuisineType;
        private String operatingHours;
        private User owner;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder cuisineType(String cuisineType) {
            this.cuisineType = cuisineType;
            return this;
        }

        public Builder operatingHours(String operatingHours) {
            this.operatingHours = operatingHours;
            return this;
        }

        public Builder owner(User owner) {
            this.owner = owner;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }
}