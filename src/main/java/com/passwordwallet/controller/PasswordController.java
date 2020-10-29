package com.passwordwallet.controller;

import com.passwordwallet.entity.PasswordEntity;
import com.passwordwallet.entity.UserEntity;
import com.passwordwallet.security.EncryptionService;
import com.passwordwallet.service.PasswordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/passwords")
public class PasswordController {

    PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
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
    public String showFormForUpdate(@RequestParam("passwordId") int id, Model model) {

        PasswordEntity password = passwordService.findById(id);
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
        password.setPassword(EncryptionService.encrypt(password.getPassword()));

        passwordService.save(password);

        return "redirect:/passwords/list";
    }

    @GetMapping("/showPassword")
    public String showPassword(@RequestParam("passwordId") int id, HttpSession session) {
        session.setAttribute("passwordToShow", id);
        return "redirect:/passwords/list";
    }
}
