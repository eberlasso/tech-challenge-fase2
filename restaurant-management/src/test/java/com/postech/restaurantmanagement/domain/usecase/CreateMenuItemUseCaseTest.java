package com.postech.restaurantmanagement.domain.usecase;

import com.postech.restaurantmanagement.domain.entity.MenuItem;
import com.postech.restaurantmanagement.domain.entity.Restaurant;
import com.postech.restaurantmanagement.domain.exception.EntityValidationException;
import com.postech.restaurantmanagement.domain.exception.ResourceAlreadyExistsException;
import com.postech.restaurantmanagement.domain.gateway.MenuItemGateway;
import com.postech.restaurantmanagement.domain.gateway.RestaurantGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateMenuItemUseCaseTest {

    @Mock
    private MenuItemGateway menuItemGateway;

    @Mock
    private RestaurantGateway restaurantGateway;

    @InjectMocks
    private CreateMenuItemUseCase useCase;

    private MenuItem validMenuItem() {
        return MenuItem.builder()
                .name("Pizza")
                .description("Cheese")
                .price(new BigDecimal("10.00"))
                .availableOnlyInRestaurant(true)
                .imagePath("/img/pizza.jpg")
                .restaurant(Restaurant.builder().id(1L).build())
                .build();
    }

    @Test
    void shouldCreateMenuItemWhenValidAndUnique() {
        MenuItem input = validMenuItem();
        MenuItem saved = MenuItem.builder()
                .id(9L)
                .name(input.getName())
                .description(input.getDescription())
                .price(input.getPrice())
                .availableOnlyInRestaurant(input.isAvailableOnlyInRestaurant())
                .imagePath(input.getImagePath())
                .restaurant(input.getRestaurant())
                .build();

        when(restaurantGateway.findById(1L)).thenReturn(Optional.of(Restaurant.builder().id(1L).build()));
        when(menuItemGateway.existsByNameAndRestaurant("Pizza", 1L)).thenReturn(false);
        when(menuItemGateway.save(input)).thenReturn(saved);

        MenuItem result = useCase.execute(input);

        assertEquals(9L, result.getId());
        verify(menuItemGateway).save(input);
    }

    @Test
    void shouldThrowWhenMenuItemInvalid() {
        MenuItem invalid = MenuItem.builder().name("X").build();

        assertThrows(EntityValidationException.class, () -> useCase.execute(invalid));
        verifyNoInteractions(restaurantGateway, menuItemGateway);
    }

    @Test
    void shouldThrowWhenRestaurantNotFound() {
        MenuItem input = validMenuItem();
        when(restaurantGateway.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityValidationException.class, () -> useCase.execute(input));
        verify(menuItemGateway, never()).save(any());
    }

    @Test
    void shouldThrowWhenMenuItemAlreadyExistsInRestaurant() {
        MenuItem input = validMenuItem();
        when(restaurantGateway.findById(1L)).thenReturn(Optional.of(Restaurant.builder().id(1L).build()));
        when(menuItemGateway.existsByNameAndRestaurant("Pizza", 1L)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> useCase.execute(input));
        verify(menuItemGateway, never()).save(any());
    }
}

