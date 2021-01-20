package com.passwordwallet.controllers;

import com.passwordwallet.entities.ActionEntity;
import com.passwordwallet.entities.UserEntity;
import com.passwordwallet.services.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping( "/actions")
public class ActionController {

    @Autowired
    ActionService actionService;

    @GetMapping("/list")
    public String actionList(Model model, HttpSession session){
        UserEntity user = (UserEntity) session.getAttribute("user");

        List<ActionEntity> actions = actionService.findAllByUser(user);

        model.addAttribute("actions", actions);

        return "actions/list";
    }

}
