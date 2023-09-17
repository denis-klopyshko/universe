package com.universe.controller;

import com.universe.dto.user.CreateUserForm;
import com.universe.dto.user.UpdateUserForm;
import com.universe.enums.UserType;
import com.universe.repository.RoleRepository;
import com.universe.service.ProfessorService;
import com.universe.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ProfessorsController {
    private final ProfessorService professorService;
    private final UserService userService;
    private final RoleRepository roleRepo;
    // private final CourseRepository courseRepo;

    @RequestMapping(value = "/professors", method = RequestMethod.GET)
    public String getList(Model model) {
        Pageable page = PageRequest.of(0, 20, Sort.by("id").ascending());
        model.addAttribute("content", "professors/professors-list");
        model.addAttribute("professors", professorService.findAll(page).getContent());
        return "index";
    }

    @PreAuthorize("hasAnyAuthority('professors::write')")
    @RequestMapping(value = "/professors/new", method = RequestMethod.GET)
    public String getCreateProfessorPage(Model model) {
        var professorForm = new CreateUserForm();
        model.addAttribute("roles", roleRepo.findAllRoleNames());
        //model.addAttribute("courses", courseRepo.findAllCourseNames());
        model.addAttribute("userType", UserType.PROFESSOR);
        model.addAttribute("content", "professors/create-professor");

        if (!model.containsAttribute("professor")) {
            model.addAttribute("professor", professorForm);
        }

        return "index";
    }

    @PreAuthorize("hasAnyAuthority('professors::write')")
    @RequestMapping(value = "/professors", method = RequestMethod.POST)
    public String createProfessor(@Valid @ModelAttribute("professor") CreateUserForm professor,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.professor", result);
            redirectAttributes.addFlashAttribute("professor", professor);
            return "redirect:/professors/new";
        }

        try {
            userService.create(professor);
        } catch (Exception e) {
            log.error("Error saving professor: " + e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("professor", professor);
            return "redirect:/professors/new";
        }

        return "redirect:/professors";
    }

    @PreAuthorize("hasAnyAuthority('professors::write')")
    @RequestMapping(value = "/professors/{id}/edit", method = RequestMethod.GET)
    public String getUpdateProfessorPage(Model model, @PathVariable("id") Long id) {
        var professor = userService.findOne(id);
        var updateProfessorForm = professor.toUpdateForm();
        model.addAttribute("roles", roleRepo.findAllRoleNames());
        //model.addAttribute("courses", courseRepo.findAllCourseNames());
        model.addAttribute("userType", UserType.PROFESSOR);
        model.addAttribute("content", "professors/edit-professor");

        if (!model.containsAttribute("professor")) {
            model.addAttribute("professor", updateProfessorForm);
        }

        return "index";
    }

    @PreAuthorize("hasAnyAuthority('professors::write')")
    @RequestMapping(value = "/professors/{id}/edit", method = RequestMethod.POST)
    public String updateProfessor(@PathVariable("id") Long id,
                             @Valid @ModelAttribute("professor") UpdateUserForm professor,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.professor", result);
            redirectAttributes.addFlashAttribute("professor", professor);
            return "redirect:/professors/{id}/edit";
        }

        try {
            userService.update(id, professor);
        } catch (Exception e) {
            log.error("Error updating professor" + e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("professor", professor);
            return "redirect:/professors/{id}/edit";
        }

        return "redirect:/professors";
    }

    @PreAuthorize("hasAnyAuthority('professors::write')")
    @RequestMapping(value = "/professors/{id}/delete", method = RequestMethod.GET)
    public String deleteProfessor(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/professors";
    }
}
