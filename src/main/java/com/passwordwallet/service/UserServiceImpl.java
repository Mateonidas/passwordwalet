package com.passwordwallet.service;

import com.passwordwallet.dao.UserRepository;
import com.passwordwallet.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity findById(int id) {
        Optional<UserEntity> result = userRepository.findById(id);

        UserEntity userEntity;

        if(result.isPresent()){
            userEntity = result.get();
        } else {
            throw new RuntimeException("Did not find user id - " + id);
        }

        return userEntity;
    }

    @Override
    public void save(UserEntity user) {
        userRepository.save(user);
    }

    @Override
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }
}
