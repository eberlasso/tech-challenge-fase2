package com.postech.restaurantmanagement.infrastructure.persistence.mapper;

import com.postech.restaurantmanagement.domain.entity.MenuItem;
import com.postech.restaurantmanagement.domain.entity.Restaurant;
import com.postech.restaurantmanagement.domain.entity.User;
import com.postech.restaurantmanagement.domain.entity.UserRole;
import com.postech.restaurantmanagement.infrastructure.persistence.entity.MenuItemEntity;
import com.postech.restaurantmanagement.infrastructure.persistence.entity.RestaurantEntity;
import com.postech.restaurantmanagement.infrastructure.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.lang.reflect.Field;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PersistenceMappersTest {

    private final UserEntityMapper userEntityMapper = Mappers.getMapper(UserEntityMapper.class);
    private final RestaurantEntityMapper restaurantEntityMapper = Mappers.getMapper(RestaurantEntityMapper.class);
    private final MenuItemEntityMapper menuItemEntityMapper = Mappers.getMapper(MenuItemEntityMapper.class);

    @BeforeEach
    void wireMapStructDependenciesForUnitTest() throws Exception {
        injectField(restaurantEntityMapper, "userEntityMapper", userEntityMapper);
        injectField(menuItemEntityMapper, "restaurantEntityMapper", restaurantEntityMapper);
    }

    private void injectField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @Test
    void shouldExposeMapStructStaticInstanceField() {
        assertNotNull(UserEntityMapper.INSTANCE);
    }

    @Test
    void shouldConvertUserEntityAndDomain() {
        UserEntity entity = new UserEntity(1L, "John", "john@email.com", "pwd", "1199", 1L);
        User domain = userEntityMapper.toDomain(entity);

        assertEquals(1L, domain.getId());
        assertTrue(domain.getRoles().contains(UserRole.RESTAURANT_OWNER));

        User toPersist = User.builder()
                .id(2L)
                .name("Client")
                .email("c@email.com")
                .password("pwd")
                .phoneNumber("1188")
                .roles(Set.of(UserRole.CLIENT))
                .build();

        UserEntity mapped = userEntityMapper.toEntity(toPersist);
        assertEquals(2L, mapped.getId());
        assertEquals(2L, mapped.getUserTypeId());
    }

    @Test
    void shouldCoverUserTypeIdConversionHelpers() {
        assertTrue(userEntityMapper.userTypeIdToRoles(null).isEmpty());
        assertTrue(userEntityMapper.userTypeIdToRoles(999L).isEmpty());
        assertTrue(userEntityMapper.userTypeIdToRoles(2L).contains(UserRole.CLIENT));
        assertEquals(2L, userEntityMapper.rolesToUserTypeId(null));
        assertEquals(2L, userEntityMapper.rolesToUserTypeId(Set.of()));
        assertEquals(1L, userEntityMapper.rolesToUserTypeId(Set.of(UserRole.RESTAURANT_OWNER)));
    }

    @Test
    void shouldConvertRestaurantAndMenuEntities() {
        UserEntity owner = new UserEntity(7L, "Owner", "o@email.com", "pwd", null, 1L);
        RestaurantEntity restaurantEntity = new RestaurantEntity(3L, "Bistro", "Main", "French", "10-22", owner);

        Restaurant restaurantDomain = restaurantEntityMapper.toDomain(restaurantEntity);
        assertEquals(3L, restaurantDomain.getId());
        assertEquals(7L, restaurantDomain.getOwner().getId());

        RestaurantEntity roundTripRestaurant = restaurantEntityMapper.toEntity(restaurantDomain);
        assertEquals("Bistro", roundTripRestaurant.getName());

        MenuItemEntity menuEntity = new MenuItemEntity(5L, "Pizza", "Cheese", new BigDecimal("10.00"), true, "/img", restaurantEntity);
        MenuItem menuDomain = menuItemEntityMapper.toDomain(menuEntity);
        assertEquals(5L, menuDomain.getId());
        assertEquals(3L, menuDomain.getRestaurant().getId());

        MenuItemEntity roundTripMenu = menuItemEntityMapper.toEntity(menuDomain);
        assertEquals("Pizza", roundTripMenu.getName());
        assertEquals(3L, roundTripMenu.getRestaurant().getId());

        assertNull(restaurantEntityMapper.toDomain(null));
        assertNull(restaurantEntityMapper.toEntity(null));
        assertNull(menuItemEntityMapper.toDomain(null));
        assertNull(menuItemEntityMapper.toEntity(null));
        assertNull(userEntityMapper.toDomain(null));
        assertNull(userEntityMapper.toEntity(null));
    }
}

