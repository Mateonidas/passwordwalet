package com.passwordwallet.services;

import com.passwordwallet.entities.PasswordBackupEntity;
import com.passwordwallet.entities.PasswordEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PasswordBackupService {
    PasswordBackupEntity findById(int id);
    List<PasswordBackupEntity> findAllByPasswordEntity(PasswordEntity passwordEntity);
    PasswordBackupEntity save(PasswordBackupEntity password);
}
