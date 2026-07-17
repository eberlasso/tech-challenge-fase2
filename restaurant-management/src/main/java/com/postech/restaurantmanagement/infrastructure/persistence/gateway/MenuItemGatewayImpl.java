package com.postech.restaurantmanagement.infrastructure.persistence.gateway;

import com.postech.restaurantmanagement.domain.entity.MenuItem;
import com.postech.restaurantmanagement.domain.gateway.MenuItemGateway;
import com.postech.restaurantmanagement.infrastructure.persistence.entity.MenuItemEntity;
import com.postech.restaurantmanagement.infrastructure.persistence.mapper.MenuItemEntityMapper;
import com.postech.restaurantmanagement.infrastructure.persistence.repository.SpringDataMenuItemRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
    public List<MenuItem> findAll() {
        return repository.findAll().stream()
                .map(menuItemMapper::toDomain)
                .toList();
    }

    @Override
    public List<MenuItem> findByRestaurantId(Long restaurantId) {
        return repository.findByRestaurantId(restaurantId).stream()
                .map(menuItemMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByNameAndRestaurant(String name, Long restaurantId) {
        return repository.existsByNameIgnoreCaseAndRestaurantId(name, restaurantId);
    }

    @Override
    public boolean existsByNameAndRestaurantAndIdNot(String name, Long restaurantId, Long id) {
        return repository.existsByNameIgnoreCaseAndRestaurantIdAndIdNot(name, restaurantId, id);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}