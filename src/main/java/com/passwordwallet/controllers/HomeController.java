package com.passwordwallet.controllers;

import com.passwordwallet.entities.IpAddressEntity;
import com.passwordwallet.entities.LoginEntity;
import com.passwordwallet.entities.UserEntity;
import com.passwordwallet.services.IpAddressService;
import com.passwordwallet.services.LoginService;
import com.passwordwallet.services.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping
public class HomeController {

    private final UserService userService;
    private final LoginService loginService;
    private final IpAddressService ipAddressService;

    public HomeController(UserService userService, LoginService loginService, IpAddressService ipAddressService) {
        this.userService = userService;
        this.loginService = loginService;
        this.ipAddressService = ipAddressService;
    }

    //Mapping for login page
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    //Mapping for home page
    @GetMapping("/")
    public String home(HttpSession session) {

        //Get user login
        String login = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        UserEntity user = userService.findByLogin(login);

        //Save user in session
        session.setAttribute("user", user);
        //Clear passwordToShow attribute to hide all passwords on display again
        session.setAttribute("passwordToShow", null);

        LoginEntity lastSuccess = loginService.findLastTimestampWithSuccess();
        LoginEntity lastFailure = loginService.findLastTimestampWithFailure();

        List<IpAddressEntity> blockedIpAddresses = ipAddressService.findBlockedIpAddresses();

        //TODO change time format
        session.setAttribute("lastSuccess", lastSuccess.getTime());

        if(lastFailure != null) {
            session.setAttribute("lastFailure", lastFailure.getTime());
        } else {
            session.setAttribute("lastFailure", "No data");
        }

        session.setAttribute("blockedIpAddresses", blockedIpAddresses);

        return "home";
    }

    @GetMapping("/unlockIP")
    public String unlockIp(@RequestParam("ipId") int id){

        IpAddressEntity blockedIpAddress = ipAddressService.findById(id);
        blockedIpAddress.setIncorrectLoginTrial(0);

        ipAddressService.save(blockedIpAddress);

        return "redirect:";
    }
}
