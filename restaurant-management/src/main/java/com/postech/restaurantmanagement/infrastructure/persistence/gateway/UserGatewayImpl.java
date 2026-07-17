package com.postech.restaurantmanagement.infrastructure.persistence.gateway;

import com.postech.restaurantmanagement.domain.entity.User;
import com.postech.restaurantmanagement.domain.gateway.UserGateway;
import com.postech.restaurantmanagement.infrastructure.persistence.entity.UserEntity;
import com.postech.restaurantmanagement.infrastructure.persistence.mapper.UserEntityMapper;
import com.postech.restaurantmanagement.infrastructure.persistence.repository.SpringDataUserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserGatewayImpl implements UserGateway {

    private final SpringDataUserRepository repository;
    private final UserEntityMapper userMapper; // Injeção do componente gerado pelo MapStruct

    public UserGatewayImpl(SpringDataUserRepository repository, UserEntityMapper userMapper) {
        this.repository = repository;
        this.userMapper = userMapper;
    }

    @Override
    public User save(User user) {
        UserEntity entityInput = userMapper.toEntity(user);
        UserEntity savedEntity = repository.save(entityInput);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id)
                .map(userMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream()
                .map(userMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByEmailAndIdNot(String email, Long id) {
        return repository.existsByEmailIgnoreCaseAndIdNot(email, id);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}