package com.passwordwallet.controller;

import com.passwordwallet.entity.UserEntity;
import com.passwordwallet.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public String listUsers(Model model){

        List<UserEntity> users = userService.findAll();
        model.addAttribute("users", users);

        return "users/list-users";
    }
}
