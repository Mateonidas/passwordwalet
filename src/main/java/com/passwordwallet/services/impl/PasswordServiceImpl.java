package com.passwordwallet.services.impl;

import com.passwordwallet.entities.PasswordEntity;
import com.passwordwallet.repositories.PasswordRepository;
import com.passwordwallet.services.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PasswordServiceImpl implements PasswordService {

    private final PasswordRepository passwordRepository;

    @Autowired
    public PasswordServiceImpl(PasswordRepository passwordRepository) {
        this.passwordRepository = passwordRepository;
    }

    @Override
    public PasswordEntity findById(int id) {
        Optional<PasswordEntity> result = passwordRepository.findById(id);

        PasswordEntity passwordEntity;

        if(result.isPresent()){
            passwordEntity = result.get();
        } else {
            throw new RuntimeException("Did not find user id - " + id);
        }

        return passwordEntity;
    }

    @Override
    public boolean existsById(int id) {
        return passwordRepository.existsById(id);
    }

    @Override
    public List<PasswordEntity> findAllByIdUser(int idUser) {
        return passwordRepository.findAllByIdUser(idUser);
    }

    @Override
    public List<PasswordEntity> findAll() {
        return passwordRepository.findAll();
    }

    @Override
    public void save(PasswordEntity password) {
        passwordRepository.save(password);
    }

    @Override
    public void saveAll(List<PasswordEntity> passwords){
        passwordRepository.saveAll(passwords);
    }

    @Override
    public void deleteById(int id) {
        PasswordEntity passwordEntity = findById(id);
        passwordEntity.setIsDeleted(true);
        save(passwordEntity);
    }
}
