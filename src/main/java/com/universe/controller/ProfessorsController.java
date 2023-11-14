package com.universe.controller;

import com.universe.dto.professor.ProfessorDto;
import com.universe.dto.user.CreateUserForm;
import com.universe.dto.user.UpdateUserForm;
import com.universe.enums.UserType;
import com.universe.repository.CourseRepository;
import com.universe.repository.RoleRepository;
import com.universe.service.ProfessorService;
import com.universe.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/professors")
public class ProfessorsController {
    private static final String HAS_READ_PERMISSION = "hasAnyAuthority('professors::write', 'professors::read', 'users::write', 'users::read')";
    private static final String HAS_WRITE_PERMISSION = "hasAnyAuthority('professors::write', 'users::write')";

    private final ProfessorService professorService;
    private final UserService userService;
    private final RoleRepository roleRepo;
    private final CourseRepository courseRepo;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE = 1;

    @PreAuthorize(HAS_READ_PERMISSION)
    @GetMapping
    public String getList(Model model,
                          @RequestParam("page") Optional<Integer> page,
                          @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(DEFAULT_PAGE);
        int pageSize = size.orElse(DEFAULT_PAGE_SIZE);

        Page<ProfessorDto> professors = professorService.findAll(
                PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.ASC, "id"))
        );
        model.addAttribute("professors", professors);

        int totalPages = professors.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "professors/professors-list";
    }

    @PreAuthorize(HAS_WRITE_PERMISSION)
    @GetMapping("/new")
    public String getCreateProfessorPage(Model model) {
        var professorForm = new CreateUserForm();
        System.out.println(professorForm);
        model.addAttribute("roles", roleRepo.findAllRoleNames());
        model.addAttribute("courses", courseRepo.findAllCourseNames());
        model.addAttribute("userType", UserType.PROFESSOR);

        if (!model.containsAttribute("professor")) {
            model.addAttribute("professor", professorForm);
        }

        return "professors/create-professor";
    }

    @PreAuthorize(HAS_WRITE_PERMISSION)
    @PostMapping
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
            log.error("Error saving professor: [{}]", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("professor", professor);
            return "redirect:/professors/new";
        }

        return "redirect:/professors";
    }

    @PreAuthorize(HAS_WRITE_PERMISSION)
    @GetMapping("/{id}/edit")
    public String getUpdateProfessorPage(Model model, @PathVariable("id") Long id) {
        var professor = userService.findOne(id);
        var updateProfessorForm = professor.toUpdateForm();
        model.addAttribute("roles", roleRepo.findAllRoleNames());
        model.addAttribute("courses", courseRepo.findAllCourseNames());
        model.addAttribute("userType", UserType.PROFESSOR);

        if (!model.containsAttribute("professor")) {
            model.addAttribute("professor", updateProfessorForm);
        }

        return "professors/edit-professor";
    }

    @PreAuthorize(HAS_WRITE_PERMISSION)
    @PostMapping("/{id}/edit")
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
            log.error("Error updating professor: [{}]", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("professor", professor);
            return "redirect:/professors/{id}/edit";
        }

        return "redirect:/professors";
    }

    @PreAuthorize(HAS_WRITE_PERMISSION)
    @GetMapping("/{id}/delete")
    public String deleteProfessor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.delete(id);
        } catch (Exception e) {
            log.error("Error deleting professor: [{}]", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("toastError", e.getLocalizedMessage());
            return "redirect:/professors";
        }

        redirectAttributes.addFlashAttribute("toastMessage", "Professor successfully deleted!");
        return "redirect:/professors";
    }
}
