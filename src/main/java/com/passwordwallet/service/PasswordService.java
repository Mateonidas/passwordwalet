package com.passwordwallet.service;

import com.passwordwallet.entity.PasswordEntity;

import java.util.List;

public interface PasswordService {

    List<PasswordEntity> findAll();

    PasswordEntity findById(int id);

    List<PasswordEntity> findAllByIdUser(int idUser);

    void save(PasswordEntity password);

    void deleteById(int id);

    void saveAll(List<PasswordEntity> passwords);
}
