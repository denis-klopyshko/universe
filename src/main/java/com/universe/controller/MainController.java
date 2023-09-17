package com.universe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String entryPoint(Model model) {
        model.addAttribute("content", "fragments/welcome");
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login/login";
    }
}
