package com.postech.restaurantmanagement.infrastructure.controller.mapper;

import com.postech.restaurantmanagement.infrastructure.controller.dto.CreateUserRequest;
import com.postech.restaurantmanagement.infrastructure.controller.dto.UserResponse;
import com.postech.restaurantmanagement.domain.entity.User;
import com.postech.restaurantmanagement.domain.entity.UserRole;
import org.mapstruct.Mapper;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // MapStruct will generate implementation for this method and will use
    // mapRolesToStrings to convert Set<UserRole> -> Set<String>
    UserResponse toResponse(User user);

    // We implement toDomain manually because the domain uses a manual Builder
    default User toDomain(CreateUserRequest req) {
        Set<UserRole> roles = req.roles() == null ? Set.of() : req.roles().stream()
                .map(r -> UserRole.valueOf(r.toUpperCase()))
                .collect(Collectors.toSet());

        return User.builder()
                .name(req.name())
                .email(req.email())
                .phoneNumber(req.phoneNumber())
                .roles(roles)
                .build();
    }

    // Helper used by MapStruct when mapping User -> UserResponse
    default Set<String> mapRolesToStrings(Set<UserRole> roles) {
        if (roles == null) return Set.of();
        return roles.stream().map(Enum::name).collect(Collectors.toSet());
    }
}


