package com.postech.restaurantmanagement.domain.entity;

import java.time.LocalDateTime;

/**
 * Enterprise Business Entity representing a Restaurant Reservation.
 * Contains core business rules for schedule validation and capacity assignment.
 * Encapsulates data without reliance on any framework external dependencies.
 */
public class Reservation {

    private final Long id;
    private final User client;
    private final Restaurant restaurant;
    private final LocalDateTime reservationDateTime;
    private final int numberOfPeople;
    private ReservationStatus status;

    private Reservation(Builder builder) {
        this.id = builder.id;
        this.client = builder.client;
        this.restaurant = builder.restaurant;
        this.reservationDateTime = builder.reservationDateTime;
        this.numberOfPeople = builder.numberOfPeople;
        this.status = builder.status != null ? builder.status : ReservationStatus.PENDING;
    }

    // --- CORE BUSINESS RULES ---

    public boolean isValid() {
        return client != null && client.getId() != null &&
               restaurant != null && restaurant.getId() != null &&
               numberOfPeople > 0 &&
               reservationDateTime != null && reservationDateTime.isAfter(LocalDateTime.now());
    }

    /**
     * Updates the current status of the reservation following structural business constraints.
     *
     * @param newStatus The targeted state transitioning assignment variable.
     * @throws IllegalArgumentException if the provided status argument is null.
     */
    public void updateStatus(ReservationStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }
        this.status = newStatus;
    }

    // --- PURE GETTERS ---

    public Long getId() { return id; }
    public User getClient() { return client; }
    public Restaurant getRestaurant() { return restaurant; }
    public LocalDateTime getReservationDateTime() { return reservationDateTime; }
    public int getNumberOfPeople() { return numberOfPeople; }
    public ReservationStatus getStatus() { return status; }

    // --- MANUAL BUILDER PATTERN ---

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private User client;
        private Restaurant restaurant;
        private LocalDateTime reservationDateTime;
        private int numberOfPeople;
        private ReservationStatus status;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder client(User client) {
            this.client = client;
            return this;
        }

        public Builder restaurant(Restaurant restaurant) {
            this.restaurant = restaurant;
            return this;
        }

        public Builder reservationDateTime(LocalDateTime reservationDateTime) {
            this.reservationDateTime = reservationDateTime;
            return this;
        }

        public Builder numberOfPeople(int numberOfPeople) {
            this.numberOfPeople = numberOfPeople;
            return this;
        }

        public Builder status(ReservationStatus status) {
            this.status = status;
            return this;
        }

        public Reservation build() {
            return new Reservation(this);
        }
    }
}