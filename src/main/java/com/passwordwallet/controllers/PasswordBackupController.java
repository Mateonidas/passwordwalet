package com.passwordwallet.controllers;

import com.passwordwallet.entities.ActionEntity;
import com.passwordwallet.entities.PasswordBackupEntity;
import com.passwordwallet.entities.PasswordEntity;
import com.passwordwallet.entities.UserEntity;
import com.passwordwallet.security.EncryptionService;
import com.passwordwallet.services.ActionService;
import com.passwordwallet.services.PasswordBackupService;
import com.passwordwallet.services.PasswordService;
import com.passwordwallet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/backup")
public class PasswordBackupController {

    @Autowired
    PasswordBackupService passwordBackupService;

    @Autowired
    PasswordService passwordService;

    @Autowired
    ActionService actionService;

    @Autowired
    UserService userService;

    @GetMapping("/listBackup")
    public String listBackup(@RequestParam("passwordId") int id, Model model, HttpSession session){
        List<PasswordBackupEntity> passwords = passwordBackupService.findAllByPasswordEntity(passwordService.findById(id));

        UserEntity user = (UserEntity) session.getAttribute("user");
        hidePasswords(passwords, session, user);
        model.addAttribute("passwords", passwords);

        return "backup/list";
    }

    @GetMapping("/chooseBackup")
    public String chooseBackup(@RequestParam("passwordId") int id){
        PasswordBackupEntity backup = passwordBackupService.findById(id);

        PasswordEntity password = backup.getPasswordEntity();
        password.setDescription(backup.getDescription());
        password.setLogin(backup.getLogin());
        password.setPassword(backup.getPassword());
        password.setWebAddress(backup.getWebAddress());
        password.setIsDeleted(false);
        passwordService.save(password);

        Date date = new Date();
        ActionEntity action = new ActionEntity();
        action.setTime(new Timestamp(date.getTime()));
        action.setUser(password.getUserByIdUser());
        action.setActionName("BACKUP");
        actionService.save(action);

        return "redirect:/passwords/list";
    }

    public List<PasswordBackupEntity> hidePasswords(List<PasswordBackupEntity> passwords, HttpSession session, UserEntity user) {

        if (session.getAttribute("passwordToShow") != null) {
            int id = (int) session.getAttribute("passwordToShow");

            passwords.forEach(password -> {
                if (password.getId() == id) {
                    try {
                        if (password.getPasswordEntity().getIdUser() != user.getId()) {
                            password.setPassword(
                                    EncryptionService.decryptAES(password.getPassword(),
                                            userService.findById(password.getPasswordEntity().getIdUser()).getPasswordHash()
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
            passwords.forEach(password -> {
                password.setPassword("**********");
            });
        }

        return passwords;
    }

}
