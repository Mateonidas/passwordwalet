package com.passwordwallet.security;

import com.passwordwallet.entities.LoginEntity;
import com.passwordwallet.entities.UserEntity;
import com.passwordwallet.services.LoginService;
import com.passwordwallet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

@Component
public class CustomAuthenticationProvider  implements AuthenticationProvider {

    UserService userService;
    LoginService loginService;

    public CustomAuthenticationProvider(UserService userService, LoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
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

        LoginEntity loginEntity = new LoginEntity();
        loginEntity.setUserByUserId(user);
        loginEntity.setTime(new Timestamp(System.currentTimeMillis()));

        LoginEntity lastFailure = loginService.findLastTimestampWithFailure();

//        String ip = getRequestRemoteAddr();

        if(!checkIfBlocked(lastFailure.getTime(), user.getIncorrectLogins())){
            throw new DisabledException("User is blocked.");
        }

        //Compare the provided password with the user's password
        if(EncryptionService.encryptMasterPassword(password, user.getSalt(), user.getUsedAlgorithm())
            .equals(user.getPasswordHash())){
            EncryptionService.setPlainPassword(password);

            user.setIncorrectLogins(0);
            userService.save(user);

            loginEntity.setResult(true);
            loginService.save(loginEntity);

            return new UsernamePasswordAuthenticationToken(login, password, authentication.getAuthorities());
        }

        user.setIncorrectLogins(user.getIncorrectLogins()+1);
        userService.save(user);

        loginEntity.setResult(false);
        loginService.save(loginEntity);

        throw new BadCredentialsException("Invalid username or password.");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private boolean checkIfBlocked(Timestamp time, int attempts){

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        if(attempts == 2) {
            int sec = 5;
            Timestamp later = new Timestamp(time.getTime() + (sec * 1000L));
            return currentTime.after(later);
        }
        else if (attempts == 3) {
            int sec = 10;
            Timestamp later = new Timestamp(time.getTime() + (sec * 1000L));
            return currentTime.after(later);
        }
        else if (attempts >= 4) {
            int sec = 120;
            Timestamp later = new Timestamp(time.getTime() + (sec * 1000L));
            return currentTime.after(later);
        }

        return true;
    }

    public static String getRequestRemoteAddr(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        return request.getRemoteAddr();
    }
}
