package com.passwordwallet.controller;

import com.passwordwallet.entity.UserEntity;
import com.passwordwallet.service.UserService;
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

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String home(HttpSession session) {

        String login = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        UserEntity user = userService.findByLogin(login).get();

        session.setAttribute("user", user);
        session.setAttribute("passwordToShow", null);

        return "home";
    }
}
