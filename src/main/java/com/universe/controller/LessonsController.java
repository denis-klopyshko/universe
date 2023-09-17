package com.universe.controller;

import com.universe.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LessonsController {
    private final LessonService lessonService;

    @GetMapping({"/lessons"})
    public String getLessonsList(Model model) {
        model.addAttribute("content", "lessons/lessons-list");
        model.addAttribute("lessons", lessonService.findAll());
        return "index";
    }
}
