package com.postech.restaurantmanagement.domain.entity;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldBuildValidUserAndExposeFields() {
        User user = User.builder()
                .id(1L)
                .name("John")
                .email("john@email.com")
                .password("Secret123")
                .phoneNumber("11999999999")
                .roles(Set.of(UserRole.CLIENT, UserRole.RESTAURANT_OWNER))
                .build();

        assertEquals(1L, user.getId());
        assertEquals("John", user.getName());
        assertEquals("john@email.com", user.getEmail());
        assertEquals("Secret123", user.getPassword());
        assertEquals("11999999999", user.getPhoneNumber());
        assertTrue(user.hasRole(UserRole.CLIENT));
        assertTrue(user.hasRole(UserRole.RESTAURANT_OWNER));
        assertTrue(user.isValid());
    }

    @Test
    void shouldBeInvalidWhenEmailOrPasswordOrRolesAreInvalid() {
        User invalidEmail = User.builder()
                .name("John")
                .email("invalid")
                .password("Secret123")
                .roles(Set.of(UserRole.CLIENT))
                .build();

        User invalidPassword = User.builder()
                .name("John")
                .email("john@email.com")
                .password(" ")
                .roles(Set.of(UserRole.CLIENT))
                .build();

        User invalidRoles = User.builder()
                .name("John")
                .email("john@email.com")
                .password("Secret123")
                .roles(Set.of())
                .build();

        assertFalse(invalidEmail.isValid());
        assertFalse(invalidPassword.isValid());
        assertFalse(invalidRoles.isValid());
    }

    @Test
    void shouldSupportAddingSingleRoleWithBuilderRoleMethod() {
        User user = User.builder()
                .name("A")
                .email("a@a.com")
                .password("p")
                .role(UserRole.ADMIN)
                .build();

        assertTrue(user.hasRole(UserRole.ADMIN));
    }
}

