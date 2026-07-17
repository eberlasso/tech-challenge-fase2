package com.postech.restaurantmanagement.infrastructure.persistence.gateway;

import com.postech.restaurantmanagement.domain.entity.Restaurant;
import com.postech.restaurantmanagement.domain.gateway.RestaurantGateway;
import com.postech.restaurantmanagement.infrastructure.persistence.entity.RestaurantEntity;
import com.postech.restaurantmanagement.infrastructure.persistence.mapper.RestaurantEntityMapper;
import com.postech.restaurantmanagement.infrastructure.persistence.repository.SpringDataRestaurantRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Infrastructure implementation acting as the boundary Gateway adapter for Restaurant relational data.
 * Fully honors the Domain contract by implementing all mandatory query structures.
 */
@Component
public class RestaurantGatewayImpl implements RestaurantGateway {

    private final SpringDataRestaurantRepository repository;
    private final RestaurantEntityMapper restaurantMapper;

    public RestaurantGatewayImpl(SpringDataRestaurantRepository repository, RestaurantEntityMapper restaurantMapper) {
        this.repository = repository;
        this.restaurantMapper = restaurantMapper;
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        RestaurantEntity entityInput = restaurantMapper.toEntity(restaurant);
        RestaurantEntity savedEntity = repository.save(entityInput);
        return restaurantMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Restaurant> findById(Long id) {
        return repository.findById(id)
                .map(restaurantMapper::toDomain);
    }

    @Override
    public List<Restaurant> findAll() { // CORRIGIDO AQUI!
        return repository.findAll().stream()
                .map(restaurantMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Restaurant> findByCuisineType(String cuisineType) {
        return repository.findByCuisineType(cuisineType).stream()
                .map(restaurantMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByNameAndAddress(String name, String address) {
        return repository.existsByNameIgnoreCaseAndAddressIgnoreCase(name, address);
    }

    @Override
    public boolean existsByNameAndAddressAndIdNot(String name, String address, Long id) {
        return repository.existsByNameIgnoreCaseAndAddressIgnoreCaseAndIdNot(name, address, id);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}