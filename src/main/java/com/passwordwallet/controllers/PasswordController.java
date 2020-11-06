package com.passwordwallet.controllers;

import com.passwordwallet.entities.PasswordEntity;
import com.passwordwallet.entities.UserEntity;
import com.passwordwallet.objects.NewPassword;
import com.passwordwallet.security.EncryptionService;
import com.passwordwallet.services.PasswordService;
import com.passwordwallet.services.UserService;
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

    //Mapping for page with list of passwords
    @GetMapping("/list")
    public String listPasswords(Model model, HttpSession session) {

        //Get user from session
        UserEntity user = (UserEntity) session.getAttribute("user");
        //Get users passwords
        List<PasswordEntity> passwords = passwordService.findAllByIdUser(user.getId());
        //Hide passwords while viewing
        EncryptionService.hidePasswords(passwords, session);

        model.addAttribute("passwords", passwords);

        return "passwords/list";
    }

    //Mapping for page with adding new password
    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model model) {

        PasswordEntity password = new PasswordEntity();
        model.addAttribute("passwordEntity", password);

        return "passwords/password-form";
    }

    //Mapping for page with updating password password
    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("passwordId") int id, Model model, HttpSession session) throws Exception {

        PasswordEntity password = passwordService.findById(id);

        //Displaying the decrypted password during the update
        password.setPassword(EncryptionService.decryptAES(password.getPassword(), null));
        model.addAttribute("passwordEntity", password);

        //Clear passwordToShow attribute to hide all passwords on display again
        session.setAttribute("passwordToShow", null);

        return "passwords/password-form";
    }

    //Mapping for deleting password
    @GetMapping("/delete")
    public String delete(@RequestParam("passwordId") int id) {
        passwordService.deleteById(id);

        return "redirect:/passwords/list";
    }

    //Mapping for saving password
    @PostMapping("/save")
    public String save(@ModelAttribute("passwordEntity") PasswordEntity password, HttpSession session) throws Exception {
        UserEntity user = (UserEntity) session.getAttribute("user");
        password.setUserByIdUser(user);
        //Encrypting password after it is saved
        password.setPassword(EncryptionService.encryptAES(password.getPassword(), null));

        passwordService.save(password);

        return "redirect:/passwords/list";
    }

    //Mapping a password marked as visible
    @GetMapping("/showPassword")
    public String showPassword(@RequestParam("passwordId") int id, HttpSession session) {
        session.setAttribute("passwordToShow", id);
        return "redirect:/passwords/list";
    }

    //Mapping for change master password page
    @GetMapping("/changePassword")
    public String changePassword(Model model) {
        model.addAttribute("newPassword", new NewPassword());
        return "/passwords/change-password";
    }

    //Mapping for change master password
    @PostMapping("/changePassword")
    public String changePassword(@ModelAttribute NewPassword newPassword, Model model) {

        //Get the value of the new password
        String password = newPassword.getNewPassword();
        String oldPassword = newPassword.getOldPassword();
        String algorithm = newPassword.getAlgorithm();


        //Validation of the entered data
        if (password.isEmpty() || !password.equals(newPassword.getRepeatPassword()) || !oldPassword.equals(EncryptionService.getPlainPassword())) {
            model.addAttribute("hasErrors", "true");
        } else {
            //Get user by login
            String login = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            UserEntity user = userService.findByLogin(login);
            String hashedPassword;

            //Check which algorithm was chosen
            if (algorithm.equals("SHA-512")) {
                //Generate salt
                String salt = EncryptionService.generateSalt();
                //Encryption of new master password in SHA-512
                hashedPassword = EncryptionService.encryptMasterPassword(
                        password,
                        salt,
                        algorithm);
                //Set user salt
                user.setSalt(salt);
            } else {
                //Encryption of new master password in HMAC
                hashedPassword = EncryptionService.encryptMasterPassword(
                        password,
                        "",
                        algorithm);
                //Cleat user salt
                user.setSalt("");
            }

            //Set new master password
            user.setPasswordHash(hashedPassword);
            //Set new used algorithm
            user.setUsedAlgorithm(algorithm);
            //Save modified user
            userService.save(user);

            //Get all user passwords
            List<PasswordEntity> passwords = passwordService.findAllByIdUser(user.getId());
            //Change passwords encryption due to the change of encryption of master password
            passwords = EncryptionService.changePasswordsEncryption(passwords, password);
            //Save all passwords
            passwordService.saveAll(passwords);

            model.addAttribute("hasErrors", "false");
        }

        return "/passwords/change-password";
    }
}
