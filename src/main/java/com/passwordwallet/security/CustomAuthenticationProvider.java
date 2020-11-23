package com.passwordwallet.security;

import com.passwordwallet.entities.UserEntity;
import com.passwordwallet.services.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    UserService userService;
    UserBlockingService userBlockingService;

    public CustomAuthenticationProvider(UserService userService, UserBlockingService userBlockingService) {
        this.userService = userService;
        this.userBlockingService = userBlockingService;
    }

    //The method used during user authentication
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        //Get user login and password
        String login = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserEntity user;

        //Check if user exists
        try {
            user = userService.findByLogin(login);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username or password.");
        }


        userBlockingService.init(user);

        //Compare the provided password with the user's password
        if (EncryptionService.encryptMasterPassword(password, user.getSalt(), user.getUsedAlgorithm())
                .equals(user.getPasswordHash())) {
            EncryptionService.setPlainPassword(password);

            userBlockingService.saveAfterSuccess();

            return new UsernamePasswordAuthenticationToken(login, password, authentication.getAuthorities());
        }

        userBlockingService.saveAfterFailure();

        throw new BadCredentialsException("Invalid username or password.");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
