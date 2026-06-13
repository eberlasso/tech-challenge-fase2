package com.postech.restaurantmanagement.infrastructure.persistence.mapper;

import com.postech.restaurantmanagement.domain.entity.Restaurant;
import com.postech.restaurantmanagement.infrastructure.persistence.entity.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// Dizemos ao MapStruct para USAR as regras do UserEntityMapper quando encontrar um User/UserEntity
@Mapper(componentModel = "spring", uses = { UserEntityMapper.class })
public interface RestaurantEntityMapper {

    RestaurantEntity toEntity(Restaurant domain);

    Restaurant toDomain(RestaurantEntity entity);
}