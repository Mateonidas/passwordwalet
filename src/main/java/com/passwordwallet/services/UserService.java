package com.passwordwallet.services;

import com.passwordwallet.entities.UserEntity;

import java.util.Optional;

public interface UserService {
    Optional<UserEntity> findByLogin(String login);
    void save(UserEntity user);
}
