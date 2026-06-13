package com.postech.restaurantmanagement.infrastructure.persistence.mapper;

import com.postech.restaurantmanagement.domain.entity.MenuItem;
import com.postech.restaurantmanagement.infrastructure.persistence.entity.MenuItemEntity;
import org.mapstruct.Mapper;

/**
 * Data Mapper executing translation mechanics between clean Menu Item domain entities and database tables using MapStruct.
 */
@Mapper(componentModel = "spring", uses = { RestaurantEntityMapper.class })
public interface MenuItemEntityMapper {

    MenuItemEntity toEntity(MenuItem domain);

    MenuItem toDomain(MenuItemEntity entity);
}