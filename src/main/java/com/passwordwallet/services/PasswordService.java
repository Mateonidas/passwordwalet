package com.passwordwallet.services;

import com.passwordwallet.entities.PasswordEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PasswordService {

    PasswordEntity findById(int id);
    List<PasswordEntity> findAllByIdUser(int idUser);
    List<PasswordEntity> findAll();
    void save(PasswordEntity password);
    void deleteById(int id);
    void saveAll(List<PasswordEntity> passwords);
}
