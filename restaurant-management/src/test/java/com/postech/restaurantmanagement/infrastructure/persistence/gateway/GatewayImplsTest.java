package com.postech.restaurantmanagement.infrastructure.persistence.gateway;

import com.postech.restaurantmanagement.domain.entity.MenuItem;
import com.postech.restaurantmanagement.domain.entity.Restaurant;
import com.postech.restaurantmanagement.domain.entity.User;
import com.postech.restaurantmanagement.infrastructure.persistence.entity.MenuItemEntity;
import com.postech.restaurantmanagement.infrastructure.persistence.entity.RestaurantEntity;
import com.postech.restaurantmanagement.infrastructure.persistence.entity.UserEntity;
import com.postech.restaurantmanagement.infrastructure.persistence.mapper.MenuItemEntityMapper;
import com.postech.restaurantmanagement.infrastructure.persistence.mapper.RestaurantEntityMapper;
import com.postech.restaurantmanagement.infrastructure.persistence.mapper.UserEntityMapper;
import com.postech.restaurantmanagement.infrastructure.persistence.repository.SpringDataMenuItemRepository;
import com.postech.restaurantmanagement.infrastructure.persistence.repository.SpringDataRestaurantRepository;
import com.postech.restaurantmanagement.infrastructure.persistence.repository.SpringDataUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GatewayImplsTest {

    @Mock private SpringDataUserRepository userRepository;
    @Mock private UserEntityMapper userMapper;
    @InjectMocks private UserGatewayImpl userGateway;

    @Mock private SpringDataRestaurantRepository restaurantRepository;
    @Mock private RestaurantEntityMapper restaurantMapper;
    @InjectMocks private RestaurantGatewayImpl restaurantGateway;

    @Mock private SpringDataMenuItemRepository menuItemRepository;
    @Mock private MenuItemEntityMapper menuItemMapper;
    @InjectMocks private MenuItemGatewayImpl menuItemGateway;

    @Test
    void shouldSaveAndFindUserThroughGateway() {
        User domain = User.builder().id(1L).build();
        UserEntity entity = new UserEntity();

        when(userMapper.toEntity(domain)).thenReturn(entity);
        when(userRepository.save(entity)).thenReturn(entity);
        when(userMapper.toDomain(entity)).thenReturn(domain);
        when(userRepository.findByEmail("a@a.com")).thenReturn(Optional.of(entity));
        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));

        assertEquals(domain, userGateway.save(domain));
        assertTrue(userGateway.findByEmail("a@a.com").isPresent());
        assertTrue(userGateway.findById(1L).isPresent());
    }

    @Test
    void shouldSaveAndQueryRestaurantsThroughGateway() {
        Restaurant domain = Restaurant.builder().id(1L).build();
        RestaurantEntity entity = new RestaurantEntity();

        when(restaurantMapper.toEntity(domain)).thenReturn(entity);
        when(restaurantRepository.save(entity)).thenReturn(entity);
        when(restaurantMapper.toDomain(entity)).thenReturn(domain);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(restaurantRepository.findAll()).thenReturn(List.of(entity));
        when(restaurantRepository.findByCuisineType("Italian")).thenReturn(List.of(entity));
        when(restaurantRepository.existsByNameIgnoreCaseAndAddressIgnoreCase("A", "B")).thenReturn(true);

        assertEquals(domain, restaurantGateway.save(domain));
        assertEquals(1, restaurantGateway.findAll().size());
        assertEquals(1, restaurantGateway.findByCuisineType("Italian").size());
        assertTrue(restaurantGateway.findById(1L).isPresent());
        assertTrue(restaurantGateway.existsByNameAndAddress("A", "B"));
    }

    @Test
    void shouldSaveAndQueryMenuItemsThroughGateway() {
        MenuItem domain = MenuItem.builder().id(5L).build();
        MenuItemEntity entity = new MenuItemEntity();

        when(menuItemMapper.toEntity(domain)).thenReturn(entity);
        when(menuItemRepository.save(entity)).thenReturn(entity);
        when(menuItemMapper.toDomain(entity)).thenReturn(domain);
        when(menuItemRepository.findById(5L)).thenReturn(Optional.of(entity));
        when(menuItemRepository.findByRestaurantId(2L)).thenReturn(List.of(entity));
        when(menuItemRepository.existsByNameIgnoreCaseAndRestaurantId("Pizza", 2L)).thenReturn(true);

        assertEquals(domain, menuItemGateway.save(domain));
        assertTrue(menuItemGateway.findById(5L).isPresent());
        assertEquals(1, menuItemGateway.findByRestaurantId(2L).size());
        assertEquals(1, menuItemGateway.findByRestaurantId(2L).get(0).size());
        assertTrue(menuItemGateway.existsByNameAndRestaurant("Pizza", 2L));
    }
}

