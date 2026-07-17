package com.postech.restaurantmanagement.domain.usecase;

import com.postech.restaurantmanagement.domain.entity.User;
import com.postech.restaurantmanagement.domain.gateway.UserGateway;

import java.util.List;

public class ListUsersUseCase {

    private final UserGateway userGateway;

    public ListUsersUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public List<User> execute() {
        return userGateway.findAll();
    }
}
