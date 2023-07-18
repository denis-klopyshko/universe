package com.universe.controller;

import com.universe.dto.professor.ProfessorDto;
import com.universe.service.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequiredArgsConstructor
public class ProfessorsController {
    private final ProfessorService professorService;

    @RequestMapping(value = "/professors", method = RequestMethod.GET)
    public String homePage(Model model) {
        Pageable page = PageRequest.of(0, 20, Sort.by("id").ascending());
        model.addAttribute("content", "professors");
        model.addAttribute("professors", professorService.findAll(page).getContent());
        return "index";
    }

    @RequestMapping(value = "/saveProfessor", method = RequestMethod.POST)
    public String saveProfessor(@ModelAttribute ProfessorDto professor, BindingResult errors, Model model) {
        // logic to process input data

        return "";
    }

}
