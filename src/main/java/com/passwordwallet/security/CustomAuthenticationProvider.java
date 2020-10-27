package com.passwordwallet.security;

import com.passwordwallet.dao.UserRepository;
import com.passwordwallet.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomAuthenticationProvider  implements AuthenticationProvider {

    @Autowired
    UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String login = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<UserEntity> users = userRepository.findByLogin(login);

        if(users.isEmpty()) {
            return null;
        }

        UserEntity user = users.get();

        if(EncryptionService.encryptPassword(password, user.getSalt(), user.getUsedAlgorithm())
            .equals(user.getPasswordHash())){
            return new UsernamePasswordAuthenticationToken(login, password, authentication.getAuthorities());
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
