package com.postech.restaurantmanagement.infrastructure.controller.mapper;

import com.postech.restaurantmanagement.infrastructure.controller.dto.CreateMenuItemRequest;
import com.postech.restaurantmanagement.infrastructure.controller.dto.MenuItemResponse;
import com.postech.restaurantmanagement.domain.entity.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {

    @Mapping(target = "restaurant", expression = "java(com.postech.restaurantmanagement.domain.entity.Restaurant.builder().id(request.restaurantId()).build())")
    MenuItem toDomain(CreateMenuItemRequest request);

    @Mapping(target = "restaurantId", source = "restaurant.id")
    MenuItemResponse toResponse(MenuItem item);
}


