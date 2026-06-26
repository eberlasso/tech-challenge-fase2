package com.postech.restaurantmanagement.domain.entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Enterprise Business Entity representing a System User.
 * Strictly decoupled from any framework or compile-time libraries (Lombok).
 */
public class User {
    
    private final Long id;
    private final String name;
    private final String email;
    private final String password;
    private final String phoneNumber;
    private final Set<UserRole> roles;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    // Constructor is private to enforce instantiation via the manual Builder
    private User(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.email = builder.email;
        this.password = builder.password;
        this.phoneNumber = builder.phoneNumber;
        this.roles = builder.roles != null ? Collections.unmodifiableSet(new HashSet<>(builder.roles)) : Collections.emptySet();
    }

    // --- BUSINESS RULES ---

    public boolean isValid() {
        return name != null && !name.isBlank() &&
               email != null && !email.isBlank() && EMAIL_PATTERN.matcher(email).matches() &&
               roles != null && !roles.isEmpty();
    }

    public boolean hasRole(UserRole role) {
        return roles.contains(role);
    }

    // --- PURE GETTERS (No Setters to enforce Immutability when needed) ---

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPhoneNumber() { return phoneNumber; }
    public Set<UserRole> getRoles() { return roles; }

    // --- MANUAL BUILDER PATTERN ---

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String name;
        private String email;
        private String password;
        private String phoneNumber;
        private Set<UserRole> roles = new HashSet<>();

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder role(UserRole role) {
            this.roles.add(role);
            return this;
        }

        public Builder roles(Set<UserRole> roles) {
            this.roles = roles;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
