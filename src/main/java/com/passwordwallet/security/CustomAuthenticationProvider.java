package com.passwordwallet.security;

import com.passwordwallet.entity.UserEntity;
import com.passwordwallet.service.UserService;
import com.passwordwallet.service.UserServiceImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomAuthenticationProvider  implements AuthenticationProvider {

    UserService userService;

    public CustomAuthenticationProvider(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String login = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<UserEntity> users = userService.findByLogin(login);

        if(users.isEmpty()) {
            return null;
        }

        UserEntity user = users.get();

        if(EncryptionService.encryptMasterPassword(password, user.getSalt(), user.getUsedAlgorithm())
            .equals(user.getPasswordHash())){
            EncryptionService.setPlainPassword(password);
            return new UsernamePasswordAuthenticationToken(login, password, authentication.getAuthorities());
        }

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
