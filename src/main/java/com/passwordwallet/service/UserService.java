package com.passwordwallet.service;

import com.passwordwallet.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserEntity> findAll();

    UserEntity findById(int id);

    Optional<UserEntity> findByLogin(String login);

    void save(UserEntity user);

    void deleteById(int id);

}
