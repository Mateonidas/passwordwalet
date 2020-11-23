package com.passwordwallet.security;

import com.passwordwallet.entities.IpAddressEntity;
import com.passwordwallet.entities.LoginEntity;
import com.passwordwallet.entities.UserEntity;
import com.passwordwallet.services.IpAddressService;
import com.passwordwallet.services.LoginService;
import com.passwordwallet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

@Service
public class UserBlockingService {

    @Autowired
    private HttpServletRequest request;

    UserService userService;
    LoginService loginService;
    IpAddressService ipAddressService;

    UserEntity userEntity;
    LoginEntity loginEntity;
    IpAddressEntity ipAddressEntity;

    @Autowired
    public UserBlockingService(UserService userService, LoginService loginService, IpAddressService ipAddressService) {
        this.userService = userService;
        this.loginService = loginService;
        this.ipAddressService = ipAddressService;
    }

    public void init(UserEntity user){
        userEntity = user;

        String ipAddress = request.getRemoteAddr();

        ipAddressEntity = ipAddressService.findByIpAddress(ipAddress);

        if(ipAddressEntity == null){
            ipAddressEntity = new IpAddressEntity();
            ipAddressEntity.setIpAddress(ipAddress);
            ipAddressEntity.setIncorrectLoginTrial(0);
        }

        loginEntity = new LoginEntity();
        loginEntity.setUserByUserId(user);
        loginEntity.setTime(new Timestamp(System.currentTimeMillis()));
        loginEntity.setIpAddressByIpAddressId(ipAddressEntity);
        LoginEntity lastFailure = loginService.findLastTimestampWithFailure();

        if(lastFailure != null){
            if (!checkIfBlockedByIP(lastFailure.getTime(), ipAddressEntity.getIncorrectLoginTrial())){
                throw new DisabledException("User is blocked by IP address.");
            }
            else if (!checkIfBlockedByAttempts(lastFailure.getTime(), user.getIncorrectLogins())) {
                throw new DisabledException("User is blocked.");
            }
        }
    }

    public void saveAfterSuccess(){
        userEntity.setIncorrectLogins(0);
        userService.save(userEntity);

        ipAddressEntity.setIncorrectLoginTrial(0);
        ipAddressService.save(ipAddressEntity);

        loginEntity.setResult(true);
        loginService.save(loginEntity);
    }

    public void saveAfterFailure(){
        userEntity.setIncorrectLogins(userEntity.getIncorrectLogins() + 1);
        userService.save(userEntity);

        ipAddressEntity.setIncorrectLoginTrial(ipAddressEntity.getIncorrectLoginTrial() + 1);
        ipAddressService.save(ipAddressEntity);

        loginEntity.setResult(false);
        loginService.save(loginEntity);
    }

    public boolean checkIfBlockedByAttempts(Timestamp time, int attempts) {

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        if (attempts == 2) {
            int sec = 5;
            Timestamp later = new Timestamp(time.getTime() + (sec * 1000L));
            return currentTime.after(later);
        } else if (attempts == 3) {
            int sec = 10;
            Timestamp later = new Timestamp(time.getTime() + (sec * 1000L));
            return currentTime.after(later);
        } else if (attempts >= 4) {
            int sec = 120;
            Timestamp later = new Timestamp(time.getTime() + (sec * 1000L));
            return currentTime.after(later);
        }

        return true;
    }

    public boolean checkIfBlockedByIP(Timestamp time, int attempts) {

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        if (attempts == 2) {
            int sec = 5;
            Timestamp later = new Timestamp(time.getTime() + (sec * 1000L));
            return currentTime.after(later);
        } else if (attempts == 3) {
            int sec = 10;
            Timestamp later = new Timestamp(time.getTime() + (sec * 1000L));
            return currentTime.after(later);
        } else if (attempts >= 4) {
            return false;
        }

        return true;
    }
}
