package com.passwordwallet.services;

import com.passwordwallet.entities.PasswordEntity;

import java.util.List;

public interface PasswordService {

    PasswordEntity findById(int id);
    List<PasswordEntity> findAllByIdUser(int idUser);
    void save(PasswordEntity password);
    void deleteById(int id);
    void saveAll(List<PasswordEntity> passwords);
}
