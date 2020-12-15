package com.passwordwallet.services.impl;

import com.passwordwallet.entities.LoginEntity;
import com.passwordwallet.repositories.LoginRepository;
import com.passwordwallet.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    LoginRepository loginRepository;

    @Autowired
    public LoginServiceImpl(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    @Override
    public LoginEntity save(LoginEntity loginEntity) {
        return loginRepository.save(loginEntity);
    }

    @Override
    public LoginEntity findLastTimestampWithSuccess() {
        return loginRepository.findDistinctFirstByResultOrderByTimeDesc(true);
    }

    @Override
    public LoginEntity findLastTimestampWithFailure() {
        return loginRepository.findDistinctFirstByResultOrderByTimeDesc(false);
    }
}
