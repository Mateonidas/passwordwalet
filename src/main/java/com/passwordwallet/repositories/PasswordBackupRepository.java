package com.passwordwallet.repositories;

import com.passwordwallet.entities.PasswordBackupEntity;
import com.passwordwallet.entities.PasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordBackupRepository extends JpaRepository<PasswordBackupEntity, Integer> {
    List<PasswordBackupEntity> findAllByPasswordEntity(PasswordEntity passwordEntity);
}
