package com.passwordwallet.services;

import com.passwordwallet.entities.LoginEntity;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {

    LoginEntity save(LoginEntity loginEntity);

    LoginEntity findLastTimestampWithSuccess();

    LoginEntity findLastTimestampWithFailure();
}
