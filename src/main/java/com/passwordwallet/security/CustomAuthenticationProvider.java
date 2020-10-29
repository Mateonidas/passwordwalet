package com.passwordwallet.security;

import com.passwordwallet.entities.UserEntity;
import com.passwordwallet.services.UserService;
import com.passwordwallet.services.UserServiceImpl;
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

    //The method used during user authentication
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        //Get user login and password
        String login = authentication.getName();
        String password = authentication.getCredentials().toString();

        //Check if user exists
        Optional<UserEntity> users = userService.findByLogin(login);

        if(users.isEmpty()) {
            return null;
        }

        UserEntity user = users.get();

        //Compare the provided password with the user's password
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
