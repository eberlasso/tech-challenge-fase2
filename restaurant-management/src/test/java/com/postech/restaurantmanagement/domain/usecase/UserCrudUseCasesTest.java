package com.postech.restaurantmanagement.domain.usecase;

import com.postech.restaurantmanagement.domain.entity.User;
import com.postech.restaurantmanagement.domain.entity.UserRole;
import com.postech.restaurantmanagement.domain.exception.ResourceAlreadyExistsException;
import com.postech.restaurantmanagement.domain.exception.ResourceNotFoundException;
import com.postech.restaurantmanagement.domain.gateway.AuditLogGateway;
import com.postech.restaurantmanagement.domain.gateway.UserGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCrudUseCasesTest {

    @Mock private UserGateway userGateway;
    @Mock private AuditLogGateway auditLogGateway;

    @Test
    void shouldListUsers() {
        User user = User.builder().id(1L).name("John").email("john@email.com").password("x").roles(Set.of(UserRole.CLIENT)).build();
        when(userGateway.findAll()).thenReturn(List.of(user));

        List<User> result = new ListUsersUseCase(userGateway).execute();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void shouldGetUserById() {
        User user = User.builder().id(1L).name("John").email("john@email.com").password("x").roles(Set.of(UserRole.CLIENT)).build();
        when(userGateway.findById(1L)).thenReturn(Optional.of(user));

        User result = new GetUserByIdUseCase(userGateway).execute(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void shouldThrowWhenUserNotFoundById() {
        when(userGateway.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> new GetUserByIdUseCase(userGateway).execute(1L));
    }

    @Test
    void shouldUpdateUser() {
        User current = User.builder().id(1L).name("John").email("old@email.com").password("x").roles(Set.of(UserRole.CLIENT)).build();
        User updated = User.builder().id(1L).name("John").email("new@email.com").password("x").roles(Set.of(UserRole.CLIENT)).build();

        when(userGateway.findById(1L)).thenReturn(Optional.of(current));
        when(userGateway.existsByEmailAndIdNot("new@email.com", 1L)).thenReturn(false);
        when(userGateway.save(updated)).thenReturn(updated);

        User result = new UpdateUserUseCase(userGateway, auditLogGateway).execute(updated);

        assertEquals("new@email.com", result.getEmail());
        verify(auditLogGateway).log(eq("USER_UPDATE"), contains("updated"), eq("SYSTEM_USER"));
    }

    @Test
    void shouldThrowWhenUpdatingWithDuplicateEmail() {
        User updated = User.builder().id(1L).name("John").email("new@email.com").password("x").roles(Set.of(UserRole.CLIENT)).build();
        when(userGateway.findById(1L)).thenReturn(Optional.of(updated));
        when(userGateway.existsByEmailAndIdNot("new@email.com", 1L)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> new UpdateUserUseCase(userGateway, auditLogGateway).execute(updated));
    }

    @Test
    void shouldDeleteUser() {
        User user = User.builder().id(1L).name("John").email("john@email.com").password("x").roles(Set.of(UserRole.CLIENT)).build();
        when(userGateway.findById(1L)).thenReturn(Optional.of(user));

        new DeleteUserUseCase(userGateway, auditLogGateway).execute(1L);

        verify(userGateway).deleteById(1L);
        verify(auditLogGateway).log(eq("USER_DELETE"), contains("removed"), eq("SYSTEM_USER"));
    }
}
