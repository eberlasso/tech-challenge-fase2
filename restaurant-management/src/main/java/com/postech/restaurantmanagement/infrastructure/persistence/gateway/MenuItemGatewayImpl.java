package com.postech.restaurantmanagement.infrastructure.persistence.gateway;

import com.postech.restaurantmanagement.domain.entity.MenuItem;
import com.postech.restaurantmanagement.domain.gateway.MenuItemGateway;
import com.postech.restaurantmanagement.infrastructure.persistence.entity.MenuItemEntity;
import com.postech.restaurantmanagement.infrastructure.persistence.mapper.MenuItemEntityMapper;
import com.postech.restaurantmanagement.infrastructure.persistence.repository.SpringDataMenuItemRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Infrastructure implementation acting as the boundary Gateway adapter for Menu Item relational data.
 */
@Component
public class MenuItemGatewayImpl implements MenuItemGateway {

    private final SpringDataMenuItemRepository repository;
    private final MenuItemEntityMapper menuItemMapper;

    public MenuItemGatewayImpl(SpringDataMenuItemRepository repository, MenuItemEntityMapper menuItemMapper) {
        this.repository = repository;
        this.menuItemMapper = menuItemMapper;
    }

    @Override
    public MenuItem save(MenuItem menuItem) {
        MenuItemEntity entityInput = menuItemMapper.toEntity(menuItem);
        MenuItemEntity savedEntity = repository.save(entityInput);
        return menuItemMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<MenuItem> findById(Long id) {
        return repository.findById(id)
                .map(menuItemMapper::toDomain);
    }

    @Override
    public List<List<MenuItem>> findByRestaurantId(Long restaurantId) {
        List<MenuItem> items = repository.findByRestaurantId(restaurantId).stream()
                .map(menuItemMapper::toDomain)
                .toList();

        return List.of(items); // Envelopa a lista dentro de outra lista
    }
}