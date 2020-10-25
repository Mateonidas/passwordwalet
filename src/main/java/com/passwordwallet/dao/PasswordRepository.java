package com.passwordwallet.dao;

import com.passwordwallet.entity.PasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordRepository extends JpaRepository<PasswordEntity, Integer> {
}
