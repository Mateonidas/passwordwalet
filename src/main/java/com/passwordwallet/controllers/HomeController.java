package com.passwordwallet.controllers;

import com.passwordwallet.entities.LoginEntity;
import com.passwordwallet.entities.UserEntity;
import com.passwordwallet.services.LoginService;
import com.passwordwallet.services.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping
public class HomeController {

    private UserService userService;
    private LoginService loginService;

    public HomeController(UserService userService, LoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
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

        //TODO change time format
        session.setAttribute("lastSuccess", lastSuccess.getTime());
        session.setAttribute("lastFailure", lastFailure.getTime());

        return "home";
    }
}
