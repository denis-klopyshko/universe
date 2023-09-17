package com.universe.controller;

import com.universe.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CoursesController {
    private final CourseService courseService;

    @GetMapping({"/courses"})
    public String homePage(Model model) {
        model.addAttribute("content", "courses/courses-list");
        model.addAttribute("courses", courseService.findAll());
        return "index";
    }
}
