package com.passwordwallet.repositories;

import com.passwordwallet.entities.PasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordRepository extends JpaRepository<PasswordEntity, Integer> {
    List<PasswordEntity> findAllByIdUser(int idUser);
}
