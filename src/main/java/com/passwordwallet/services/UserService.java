package com.passwordwallet.services;

import com.passwordwallet.entities.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    Optional<UserEntity> findByLogin(String login);
    void save(UserEntity user);
}
