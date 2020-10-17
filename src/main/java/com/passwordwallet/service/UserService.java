package com.passwordwallet.service;

import com.passwordwallet.entity.UserEntity;

import java.util.List;

public interface UserService {

    List<UserEntity> findAll();

    UserEntity findById(int id);

    void save(UserEntity user);

    void deleteById(int id);

}
