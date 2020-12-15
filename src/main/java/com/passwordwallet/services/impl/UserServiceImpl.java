package com.passwordwallet.services.impl;

import com.passwordwallet.entities.UserEntity;
import com.passwordwallet.repositories.UserRepository;
import com.passwordwallet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity findByLogin(String login) {

        Optional<UserEntity> result = userRepository.findByLogin(login);

        UserEntity userEntity;

        if(result.isPresent()){
            userEntity = result.get();
        } else {
            throw new RuntimeException("Did not find login - " + login);
        }

        return userEntity;

    }

    @Override
    public UserEntity findByEmail(String email) {

        Optional<UserEntity> result = userRepository.findByEmail(email);

        UserEntity userEntity;

        if(result.isPresent()){
            userEntity = result.get();
        } else {
            throw new RuntimeException("Did not find email - " + email);
        }

        return userEntity;

    }

    @Override
    public UserEntity findById(int id) {

        Optional<UserEntity> result = userRepository.findById(id);

        UserEntity userEntity;

        if(result.isPresent()){
            userEntity = result.get();
        } else {
            throw new RuntimeException("Did not find user with id - " + id);
        }

        return userEntity;

    }

    @Override
    public void save(UserEntity user) {
        userRepository.save(user);
    }

}
