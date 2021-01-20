package com.passwordwallet.controllers;

import com.passwordwallet.entities.ActionEntity;
import com.passwordwallet.entities.PasswordEntity;
import com.passwordwallet.entities.UserEntity;
import com.passwordwallet.objects.NewPassword;
import com.passwordwallet.security.EncryptionService;
import com.passwordwallet.services.ActionService;
import com.passwordwallet.services.PasswordService;
import com.passwordwallet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/passwords")
public class PasswordController {

    UserService userService;
    PasswordService passwordService;

    @Autowired
    ActionService actionService;

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
        List<PasswordEntity> userPasswords = passwordService.findAllByIdUser(user.getId());
        List<PasswordEntity> sharedPasswords = user.getSharedPasswords();

        List<PasswordEntity> passwords = new ArrayList<>();

        userPasswords.forEach(passwordEntity -> {
            if (!passwordEntity.getIsDeleted()) {
                passwords.add(new PasswordEntity(passwordEntity));
            }
        });

        sharedPasswords.forEach(passwordEntity -> {
            if (!passwordEntity.getIsDeleted()) {
                passwords.add(new PasswordEntity(passwordEntity));
            }
        });

        //Hide passwords while viewing
        hidePasswords(passwords, session, user);

        model.addAttribute("passwords", passwords);
        model.addAttribute("user", user);

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
    public String delete(@RequestParam("passwordId") int id, HttpSession session) {

        passwordService.deleteById(id);

        UserEntity user = (UserEntity) session.getAttribute("user");

        Date date = new Date();
        ActionEntity action = new ActionEntity();
        action.setTime(new Timestamp(date.getTime()));
        action.setUser(user);
        action.setActionName("DELETE");
        actionService.save(action);

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

        Date date = new Date();
        ActionEntity action = new ActionEntity();
        action.setTime(new Timestamp(date.getTime()));
        action.setUser(user);

        if (passwordService.existsById(password.getId())) {
            action.setActionName("UPDATE");
        } else {
            action.setActionName("CREATE");
        }

        actionService.save(action);

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
//            passwords = EncryptionService.changePasswordsEncryption(passwords, password);
            EncryptionService.setPlainPassword(newPassword.getNewPassword());
            passwords = EncryptionService.changePasswordsEncryption(passwords, user.getPasswordHash());
            //Save all passwords
            passwordService.saveAll(passwords);

            model.addAttribute("hasErrors", "false");
        }

        return "/passwords/change-password";
    }

    @GetMapping("/showFormForShare")
    public String showFormForShare(@RequestParam("passwordId") int id, HttpSession session) {

        PasswordEntity password = passwordService.findById(id);
        session.setAttribute("passwordToShare", password);

        return "passwords/share-form";
    }

    @PostMapping("/share")
    public String share(@RequestParam(value = "email") String email, HttpSession session) {

        PasswordEntity passwordToShare = (PasswordEntity) session.getAttribute("passwordToShare");

        try {
            UserEntity user = userService.findByEmail(email);
            List<PasswordEntity> sharedPassword = user.getSharedPasswords();
            sharedPassword.add(passwordToShare);
            user.setSharedPasswords(sharedPassword);

            userService.save(user);

            UserEntity actualUser = (UserEntity) session.getAttribute("user");

            Date date = new Date();
            ActionEntity action = new ActionEntity();
            action.setTime(new Timestamp(date.getTime()));
            action.setUser(actualUser);
            action.setActionName("SHARE");
            actionService.save(action);

            return "redirect:/passwords/list";
        } catch (RuntimeException e) {
            session.setAttribute("emailError", e.getMessage());
            return "redirect:/passwords/showFormForShare?passwordId=" + passwordToShare.getId();
        }
    }

    public List<PasswordEntity> hidePasswords(List<PasswordEntity> passwords, HttpSession session, UserEntity user) {

        //Check if any of the passwords have been marked as visible
        if (session.getAttribute("passwordToShow") != null) {
            int id = (int) session.getAttribute("passwordToShow");

            //Decrypt chosen password and hide the rest
            passwords.forEach(password -> {
                if (password.getId() == id) {
                    try {
                        if (password.getIdUser() != user.getId()) {
                            password.setPassword(
                                    EncryptionService.decryptAES(password.getPassword(),
                                            userService.findById(password.getIdUser()).getPasswordHash()
                                    ));
                        } else {
                            password.setPassword(EncryptionService.decryptAES(password.getPassword(), null));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    password.setPassword("**********");
                }
            });
        } else {
            //Hide all passwords
            passwords.forEach(password -> {
                password.setPassword("**********");
            });
        }

        return passwords;
    }
}
