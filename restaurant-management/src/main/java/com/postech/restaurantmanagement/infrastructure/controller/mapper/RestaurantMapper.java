package com.postech.restaurantmanagement.infrastructure.controller.mapper;

import com.postech.restaurantmanagement.infrastructure.controller.dto.CreateRestaurantRequest;
import com.postech.restaurantmanagement.infrastructure.controller.dto.RestaurantResponse;
import com.postech.restaurantmanagement.domain.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    @Mapping(target = "owner", expression = "java(com.postech.restaurantmanagement.domain.entity.User.builder().id(request.ownerId()).build())")
    Restaurant toDomain(CreateRestaurantRequest request);

    @Mapping(target = "ownerId", source = "owner.id")
    RestaurantResponse toResponse(Restaurant restaurant);
}


