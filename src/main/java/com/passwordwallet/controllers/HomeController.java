package com.passwordwallet.controllers;

import com.passwordwallet.entities.UserEntity;
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

    public HomeController(UserService userService) {
        this.userService = userService;
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
        UserEntity user = userService.findByLogin(login).get();

        //Save user in session
        session.setAttribute("user", user);
        //Clear passwordToShow attribute to hide all passwords on display again
        session.setAttribute("passwordToShow", null);

        return "home";
    }
}
