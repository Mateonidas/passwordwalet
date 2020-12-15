package com.passwordwallet.services;

import com.passwordwallet.entities.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserEntity findByLogin(String login);

    UserEntity findByEmail(String email);

    void save(UserEntity user);
}
