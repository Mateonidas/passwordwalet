package com.passwordwallet.repositories;

import com.passwordwallet.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByLogin(String login);

    Optional<UserEntity> findByEmail(String email);
}
