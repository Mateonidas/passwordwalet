package com.passwordwallet.repositories;

import com.passwordwallet.entities.LoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<LoginEntity, Integer> {
    LoginEntity findDistinctFirstByResultOrderByTimeDesc(Boolean result);
}
