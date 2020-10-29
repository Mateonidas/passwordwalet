package com.passwordwallet.repositories;

import com.passwordwallet.entities.PasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordRepository extends JpaRepository<PasswordEntity, Integer> {
    List<PasswordEntity> findAllByIdUser(int idUser);
}
