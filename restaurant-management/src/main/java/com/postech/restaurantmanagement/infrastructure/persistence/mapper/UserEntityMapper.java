package com.postech.restaurantmanagement.infrastructure.persistence.mapper;

import com.postech.restaurantmanagement.domain.entity.User;
import com.postech.restaurantmanagement.domain.entity.UserRole;
import com.postech.restaurantmanagement.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    UserEntityMapper INSTANCE = Mappers.getMapper(UserEntityMapper.class);

    UserEntity toEntity(User domain);

    User toDomain(UserEntity entity);

    // Mapeador customizado para converter os Enums do domínio para Strings do JPA
    @Named("mapRolesToDomain")
    default Set<UserRole> mapRolesToDomain(Set<String> roles) {
        if (roles == null) return null;
        return roles.stream().map(UserRole::valueOf).collect(Collectors.toSet());
    }

    @Named("mapRolesToEntity")
    default Set<String> mapRolesToEntity(Set<UserRole> roles) {
        if (roles == null) return null;
        return roles.stream().map(Enum::name).collect(Collectors.toSet());
    }
}