package com.passwordwallet.services.impl;

import com.passwordwallet.entities.PasswordBackupEntity;
import com.passwordwallet.entities.PasswordEntity;
import com.passwordwallet.repositories.PasswordBackupRepository;
import com.passwordwallet.repositories.PasswordRepository;
import com.passwordwallet.services.PasswordBackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PasswordBackupServiceImpl implements PasswordBackupService {

    @Autowired
    PasswordBackupRepository passwordBackupRepository;

    @Override
    public PasswordBackupEntity findById(int id) {
        Optional<PasswordBackupEntity> result = passwordBackupRepository.findById(id);

        PasswordBackupEntity passwordBackupEntity;

        if(result.isPresent()){
            passwordBackupEntity = result.get();
        } else {
            throw new RuntimeException("Did not find password id - " + id);
        }

        return passwordBackupEntity;
    }

    @Override
    public List<PasswordBackupEntity> findAllByPasswordEntity(PasswordEntity passwordEntity) {
        return passwordBackupRepository.findAllByPasswordEntity(passwordEntity);
    }

    @Override
    public PasswordBackupEntity save(PasswordBackupEntity password) {
        return passwordBackupRepository.save(password);
    }

}
