package com.universe.controller;

import com.universe.service.UserService;
import com.universe.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserService userService;

    @GetMapping("/")
    public String entryPoint(Model model) {
        var currentUserEmail = SecurityUtils.getCurrentUserLogin();
        var user = userService.findByEmail(currentUserEmail);
        model.addAttribute("user", user);
        return "fragments/welcome";
    }

    @GetMapping("/login")
    public String login() {
        return "login/login";
    }
}
