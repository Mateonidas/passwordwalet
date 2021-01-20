package com.passwordwallet.controllers;

import com.passwordwallet.entities.ActionEntity;
import com.passwordwallet.entities.PasswordBackupEntity;
import com.passwordwallet.entities.PasswordEntity;
import com.passwordwallet.services.ActionService;
import com.passwordwallet.services.PasswordBackupService;
import com.passwordwallet.services.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/listBackup")
    public String listBackup(@RequestParam("passwordId") int id, Model model){
        List<PasswordBackupEntity> passwords = passwordBackupService.findAllByPasswordEntity(passwordService.findById(id));

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

}
