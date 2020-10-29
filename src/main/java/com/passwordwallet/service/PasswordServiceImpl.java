package com.passwordwallet.service;

import com.passwordwallet.dao.PasswordRepository;
import com.passwordwallet.entity.PasswordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PasswordServiceImpl implements PasswordService {

    private PasswordRepository passwordRepository;

    @Autowired
    public PasswordServiceImpl(PasswordRepository passwordRepository) {
        this.passwordRepository = passwordRepository;
    }

    @Override
    public List<PasswordEntity> findAll() {
        return passwordRepository.findAll();
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
    public List<PasswordEntity> findAllByIdUser(int idUser) {
        return passwordRepository.findAllByIdUser(idUser);
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
        passwordRepository.deleteById(id);
    }
}
