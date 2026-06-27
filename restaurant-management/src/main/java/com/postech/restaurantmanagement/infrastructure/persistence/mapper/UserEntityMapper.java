package com.postech.restaurantmanagement.infrastructure.persistence.mapper;

import com.postech.restaurantmanagement.domain.entity.User;
import com.postech.restaurantmanagement.domain.entity.UserRole;
import com.postech.restaurantmanagement.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    UserEntityMapper INSTANCE = Mappers.getMapper(UserEntityMapper.class);

    @Mapping(target = "roles", source = "userTypeId", qualifiedByName = "userTypeIdToRoles")
    User toDomain(UserEntity entity);

    @Mapping(target = "userTypeId", source = "roles", qualifiedByName = "rolesToUserTypeId")
    UserEntity toEntity(User domain);

    // Converte userTypeId (banco) para roles (domínio)
    // Mapeamento: 1 = RESTAURANT_OWNER, 2 = CLIENT (CUSTOMER no BD)
    @Named("userTypeIdToRoles")
    default Set<UserRole> userTypeIdToRoles(Long userTypeId) {
        if (userTypeId == null) return Collections.emptySet();
        return switch (userTypeId.intValue()) {
            case 1 -> Set.of(UserRole.RESTAURANT_OWNER);
            case 2 -> Set.of(UserRole.CLIENT);  // BD: CUSTOMER -> App: CLIENT
            default -> Collections.emptySet();
        };
    }

    // Converte roles (domínio) para userTypeId (banco)
    @Named("rolesToUserTypeId")
    default Long rolesToUserTypeId(Set<UserRole> roles) {
        if (roles == null || roles.isEmpty()) return 2L; // Padrão: CLIENT/CUSTOMER
        // Se tem RESTAURANT_OWNER, retorna 1, senão 2
        return roles.contains(UserRole.RESTAURANT_OWNER) ? 1L : 2L;
    }
}