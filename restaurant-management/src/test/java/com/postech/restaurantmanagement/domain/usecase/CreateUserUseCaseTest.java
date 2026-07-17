package com.postech.restaurantmanagement.domain.usecase;

import com.postech.restaurantmanagement.domain.entity.User;
import com.postech.restaurantmanagement.domain.entity.UserRole;
import com.postech.restaurantmanagement.domain.exception.EntityValidationException;
import com.postech.restaurantmanagement.domain.exception.ResourceAlreadyExistsException;
import com.postech.restaurantmanagement.domain.gateway.UserGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserGateway userGateway;

    @InjectMocks
    private CreateUserUseCase useCase;

    @Test
    void shouldCreateUserWhenValidAndEmailNotTaken() {
        User newUser = User.builder()
                .name("John")
                .email("john@email.com")
                .password("Secret123")
                .roles(Set.of(UserRole.CLIENT))
                .build();

        User saved = User.builder()
                .id(1L)
                .name("John")
                .email("john@email.com")
                .password("Secret123")
                .roles(Set.of(UserRole.CLIENT))
                .build();

        when(userGateway.findByEmail("john@email.com")).thenReturn(Optional.empty());
        when(userGateway.save(newUser)).thenReturn(saved);

        User result = useCase.execute(newUser);

        assertEquals(1L, result.getId());
        verify(userGateway).findByEmail("john@email.com");
        verify(userGateway).save(newUser);
    }

    @Test
    void shouldThrowWhenUserInvalid() {
        User invalid = User.builder().email("invalid").build();

        assertThrows(EntityValidationException.class, () -> useCase.execute(invalid));
        verify(userGateway, never()).save(any());
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {
        User user = User.builder()
                .name("John")
                .email("john@email.com")
                .password("Secret123")
                .roles(Set.of(UserRole.CLIENT))
                .build();

        when(userGateway.findByEmail("john@email.com")).thenReturn(Optional.of(user));

        assertThrows(ResourceAlreadyExistsException.class, () -> useCase.execute(user));
        verify(userGateway, never()).save(any());
    }
}

