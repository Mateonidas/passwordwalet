package com.passwordwallet.controllers;

import com.passwordwallet.entities.UserEntity;
import com.passwordwallet.security.EncryptionService;
import com.passwordwallet.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Mapping for registration page
    @GetMapping("/showFormForRegistration")
    public String showFormForRegistration(Model model) {

        UserEntity user = new UserEntity();
        model.addAttribute("userEntity", user);

        return "users/registration";
    }

    //Mapping for saving new user
    @PostMapping("/save")
    public String save(@ModelAttribute("userEntity") UserEntity user) {

        String salt;

        //Check used algorithm and set salt
        if(user.getUsedAlgorithm().equals("SHA-512")){
            salt = EncryptionService.generateSalt();
        } else {
            salt = "";
        }

        //Encryption of user master password
        user.setPasswordHash(EncryptionService.encryptMasterPassword(
                user.getPasswordHash(),
                salt,
                user.getUsedAlgorithm()
        ));

        //Set user salt
        user.setSalt(salt);
        userService.save(user);

        return "login";
    }
}
