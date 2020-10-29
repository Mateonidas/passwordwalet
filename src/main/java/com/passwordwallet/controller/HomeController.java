package com.passwordwallet.controller;

import com.passwordwallet.entity.UserEntity;
import com.passwordwallet.objects.NewPassword;
import com.passwordwallet.security.EncryptionService;
import com.passwordwallet.service.UserService;
import org.dom4j.rule.Mode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/changePassword")
    public String changePassword(Model model){
        model.addAttribute("newPassword", new NewPassword());
        return "change-password";
    }

    @PostMapping("/changePassword")
    public String changePassword(@ModelAttribute NewPassword newPassword, Model model) {

        if(!newPassword.getNewPassword().equals(newPassword.getRepeatPassword())){
            model.addAttribute("hasErrors", "true");
        } else {
            String login = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            UserEntity user = userService.findByLogin(login).get();
            String hashedPassword;

            if(newPassword.getAlgorithm().equals("SHA-512")){
                String salt = EncryptionService.generateSalt();
                hashedPassword = EncryptionService.encryptPassword(
                        newPassword.getNewPassword(),
                        salt,
                        newPassword.getAlgorithm());
                user.setSalt(salt);
            }else{
                hashedPassword = EncryptionService.encryptPassword(
                        newPassword.getNewPassword(),
                        "",
                        newPassword.getAlgorithm());
                user.setSalt("");
            }

            user.setPasswordHash(hashedPassword);
            user.setUsedAlgorithm(newPassword.getAlgorithm());

            userService.save(user);
        }

        return "change-password";
    }
}
