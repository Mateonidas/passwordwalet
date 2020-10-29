package com.passwordwallet.controller;

import com.passwordwallet.entity.PasswordEntity;
import com.passwordwallet.entity.UserEntity;
import com.passwordwallet.objects.NewPassword;
import com.passwordwallet.security.EncryptionService;
import com.passwordwallet.service.PasswordService;
import com.passwordwallet.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/passwords")
public class PasswordController {

    UserService userService;
    PasswordService passwordService;

    public PasswordController(UserService userService, PasswordService passwordService) {
        this.userService = userService;
        this.passwordService = passwordService;
    }

    @GetMapping("/list")
    public String listPasswords(Model model, HttpSession session) {

        UserEntity user = (UserEntity) session.getAttribute("user");
        List<PasswordEntity> passwords = passwordService.findAllByIdUser(user.getId());
        EncryptionService.hidePasswords(passwords, session);

        model.addAttribute("passwords", passwords);

        return "passwords/list";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model model) {

        PasswordEntity password = new PasswordEntity();
        model.addAttribute("passwordEntity", password);

        return "passwords/password-form";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("passwordId") int id, Model model) throws Exception {

        PasswordEntity password = passwordService.findById(id);

        password.setPassword(EncryptionService.decrypt(password.getPassword()));
        model.addAttribute("passwordEntity", password);

        return "passwords/password-form";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("passwordId") int id) {
        passwordService.deleteById(id);

        return "redirect:/passwords/list";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("passwordEntity") PasswordEntity password, HttpSession session) throws Exception {
        UserEntity user = (UserEntity) session.getAttribute("user");
        password.setUserByIdUser(user);
        password.setPassword(EncryptionService.encrypt(password.getPassword(), null));

        passwordService.save(password);

        return "redirect:/passwords/list";
    }

    @GetMapping("/showPassword")
    public String showPassword(@RequestParam("passwordId") int id, HttpSession session) {
        session.setAttribute("passwordToShow", id);
        return "redirect:/passwords/list";
    }

    @GetMapping("/changePassword")
    public String changePassword(Model model) {
        model.addAttribute("newPassword", new NewPassword());
        return "change-password";
    }

    @PostMapping("/changePassword")
    public String changePassword(@ModelAttribute NewPassword newPassword, Model model) {

        if (!newPassword.getNewPassword().equals(newPassword.getRepeatPassword())) {
            model.addAttribute("hasErrors", "true");
        } else {
            String login = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            UserEntity user = userService.findByLogin(login).get();
            String hashedPassword;

            if (newPassword.getAlgorithm().equals("SHA-512")) {
                String salt = EncryptionService.generateSalt();
                hashedPassword = EncryptionService.encryptMasterPassword(
                        newPassword.getNewPassword(),
                        salt,
                        newPassword.getAlgorithm());
                user.setSalt(salt);
            } else {
                hashedPassword = EncryptionService.encryptMasterPassword(
                        newPassword.getNewPassword(),
                        "",
                        newPassword.getAlgorithm());
                user.setSalt("");
            }



            List<PasswordEntity> passwords = passwordService.findAllByIdUser(user.getId());
            EncryptionService.changePasswordsEncryption(passwords, newPassword.getNewPassword());
            passwordService.saveAll(passwords);

            user.setPasswordHash(hashedPassword);
            user.setUsedAlgorithm(newPassword.getAlgorithm());
            userService.save(user);

            model.addAttribute("hasErrors", "false");
        }

        return "change-password";
    }
}
