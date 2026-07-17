package com.postech.restaurantmanagement.domain.usecase;

import com.postech.restaurantmanagement.domain.entity.Restaurant;
import com.postech.restaurantmanagement.domain.entity.User;
import com.postech.restaurantmanagement.domain.exception.EntityValidationException;
import com.postech.restaurantmanagement.domain.exception.ResourceAlreadyExistsException;
import com.postech.restaurantmanagement.domain.gateway.AuditLogGateway;
import com.postech.restaurantmanagement.domain.gateway.RestaurantGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRestaurantUseCaseTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    @Mock
    private AuditLogGateway auditLogGateway;

    @InjectMocks
    private CreateRestaurantUseCase useCase;

    @Test
    void shouldCreateRestaurantAndLogAudit() {
        Restaurant input = Restaurant.builder()
                .name("Bistro")
                .address("Main St")
                .cuisineType("French")
                .operatingHours("10:00-22:00")
                .owner(User.builder().id(7L).build())
                .build();

        Restaurant saved = Restaurant.builder()
                .id(10L)
                .name("Bistro")
                .address("Main St")
                .cuisineType("French")
                .operatingHours("10:00-22:00")
                .owner(User.builder().id(7L).build())
                .build();

        when(restaurantGateway.existsByNameAndAddress("Bistro", "Main St")).thenReturn(false);
        when(restaurantGateway.save(input)).thenReturn(saved);

        Restaurant result = useCase.execute(input);

        assertEquals(10L, result.getId());
        verify(auditLogGateway).log(eq("RESTAURANT_CREATION"), contains("Bistro"), eq("SYSTEM_USER"));
    }

    @Test
    void shouldThrowWhenRestaurantInvalid() {
        Restaurant invalid = Restaurant.builder().name("B").build();

        assertThrows(EntityValidationException.class, () -> useCase.execute(invalid));
        verifyNoInteractions(restaurantGateway, auditLogGateway);
    }

    @Test
    void shouldThrowWhenRestaurantAlreadyExists() {
        Restaurant input = Restaurant.builder()
                .name("Bistro")
                .address("Main St")
                .cuisineType("French")
                .operatingHours("10:00-22:00")
                .owner(User.builder().id(7L).build())
                .build();

        when(restaurantGateway.existsByNameAndAddress("Bistro", "Main St")).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> useCase.execute(input));
        verify(restaurantGateway, never()).save(any());
        verifyNoInteractions(auditLogGateway);
    }
}

