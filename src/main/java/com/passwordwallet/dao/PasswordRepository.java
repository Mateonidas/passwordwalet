package com.passwordwallet.dao;

import com.passwordwallet.entity.PasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordRepository extends JpaRepository<PasswordEntity, Integer> {
    List<PasswordEntity> findAllByIdUser(int idUser);
}
