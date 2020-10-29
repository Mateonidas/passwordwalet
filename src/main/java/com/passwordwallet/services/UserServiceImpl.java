package com.passwordwallet.services;

import com.passwordwallet.entities.UserEntity;
import com.passwordwallet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserEntity> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public void save(UserEntity user) {
        userRepository.save(user);
    }

}
