package com.postech.restaurantmanagement.domain.entity;

import java.time.LocalTime;

public class Restaurant {
    private Long id;
    private String name;
    private String location; // Address, neighborhood, or city details
    private String cuisineType; // E.g., Italian, Brazilian, Japanese
    private LocalTime openingTime;
    private LocalTime closingTime;
    private Integer totalTableCapacity;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getCuisineType() {
        return cuisineType;
    }
    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }
    public LocalTime getOpeningTime() {
        return openingTime;
    }
    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }
    public LocalTime getClosingTime() {
        return closingTime;
    }
    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }
    public Integer getTotalTableCapacity() {
        return totalTableCapacity;
    }
    public void setTotalTableCapacity(Integer totalTableCapacity) {
        this.totalTableCapacity = totalTableCapacity;
    }

    // --- CORE BUSINESS RULES ---

    /**
     * Validates if all required attributes of the restaurant are present and coherent
     * before creation or persistence.
     *
     * @return true if the restaurant data is valid, false otherwise.
     */
    public boolean isValid() {
        return name != null && !name.isBlank() &&
               location != null && !location.isBlank() &&
               cuisineType != null && !cuisineType.isBlank() &&
               openingTime != null && closingTime != null &&
               openingTime.isBefore(closingTime) &&
               totalTableCapacity != null && totalTableCapacity > 0;
    }

    /**
     * Verifies if the restaurant is operational during a requested reservation time.
     *
     * @param reservationTime The desired time to check availability.
     * @return true if the requested time falls within operational hours, false otherwise.
     */
    public boolean isOpenAt(LocalTime reservationTime) {
        if (reservationTime == null) {
            return false;
        }
        return !reservationTime.isBefore(this.openingTime) && !reservationTime.isAfter(this.closingTime);
    }
}
