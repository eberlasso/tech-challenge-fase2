package com.postech.restaurantmanagement.domain.usecase;

import com.postech.restaurantmanagement.domain.entity.MenuItem;
import com.postech.restaurantmanagement.domain.entity.Restaurant;
import com.postech.restaurantmanagement.domain.exception.ResourceAlreadyExistsException;
import com.postech.restaurantmanagement.domain.exception.ResourceNotFoundException;
import com.postech.restaurantmanagement.domain.gateway.AuditLogGateway;
import com.postech.restaurantmanagement.domain.gateway.MenuItemGateway;
import com.postech.restaurantmanagement.domain.gateway.RestaurantGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuItemCrudUseCasesTest {

    @Mock private MenuItemGateway menuItemGateway;
    @Mock private RestaurantGateway restaurantGateway;
    @Mock private AuditLogGateway auditLogGateway;

    private MenuItem menuItem() {
        return MenuItem.builder()
                .id(5L)
                .name("Pizza")
                .description("Cheese")
                .price(new BigDecimal("10.00"))
                .availableOnlyInRestaurant(true)
                .imagePath("/img")
                .restaurant(Restaurant.builder().id(1L).build())
                .build();
    }

    @Test
    void shouldListMenuItemsWithAndWithoutRestaurantFilter() {
        MenuItem item = menuItem();
        when(menuItemGateway.findAll()).thenReturn(List.of(item));
        when(menuItemGateway.findByRestaurantId(1L)).thenReturn(List.of(item));

        ListMenuItemsUseCase useCase = new ListMenuItemsUseCase(menuItemGateway);
        assertEquals(1, useCase.execute(null).size());
        assertEquals(1, useCase.execute(1L).size());
    }

    @Test
    void shouldGetMenuItemById() {
        when(menuItemGateway.findById(5L)).thenReturn(Optional.of(menuItem()));
        assertEquals(5L, new GetMenuItemByIdUseCase(menuItemGateway).execute(5L).getId());
    }

    @Test
    void shouldThrowWhenMenuItemNotFound() {
        when(menuItemGateway.findById(5L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> new GetMenuItemByIdUseCase(menuItemGateway).execute(5L));
    }

    @Test
    void shouldUpdateMenuItem() {
        MenuItem item = menuItem();
        when(menuItemGateway.findById(5L)).thenReturn(Optional.of(item));
        when(restaurantGateway.findById(1L)).thenReturn(Optional.of(Restaurant.builder().id(1L).build()));
        when(menuItemGateway.existsByNameAndRestaurantAndIdNot("Pizza", 1L, 5L)).thenReturn(false);
        when(menuItemGateway.save(item)).thenReturn(item);

        MenuItem result = new UpdateMenuItemUseCase(menuItemGateway, restaurantGateway, auditLogGateway).execute(item);

        assertEquals(5L, result.getId());
        verify(auditLogGateway).log(eq("MENU_ITEM_UPDATE"), contains("updated"), eq("SYSTEM_USER"));
    }

    @Test
    void shouldThrowWhenUpdatingDuplicatedMenuItem() {
        MenuItem item = menuItem();
        when(menuItemGateway.findById(5L)).thenReturn(Optional.of(item));
        when(restaurantGateway.findById(1L)).thenReturn(Optional.of(Restaurant.builder().id(1L).build()));
        when(menuItemGateway.existsByNameAndRestaurantAndIdNot("Pizza", 1L, 5L)).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class,
                () -> new UpdateMenuItemUseCase(menuItemGateway, restaurantGateway, auditLogGateway).execute(item));
    }

    @Test
    void shouldDeleteMenuItem() {
        MenuItem item = menuItem();
        when(menuItemGateway.findById(5L)).thenReturn(Optional.of(item));

        new DeleteMenuItemUseCase(menuItemGateway, auditLogGateway).execute(5L);

        verify(menuItemGateway).deleteById(5L);
        verify(auditLogGateway).log(eq("MENU_ITEM_DELETE"), contains("removed"), eq("SYSTEM_USER"));
    }
}
