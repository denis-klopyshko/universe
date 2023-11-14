package com.universe.controller;

import com.universe.dto.student.StudentDto;
import com.universe.dto.user.CreateUserForm;
import com.universe.dto.user.UpdateUserForm;
import com.universe.enums.UserType;
import com.universe.repository.CourseRepository;
import com.universe.repository.GroupRepository;
import com.universe.repository.RoleRepository;
import com.universe.service.StudentService;
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
@RequestMapping("/students")
public class StudentsController {
    private static final String HAS_READ_PERMISSION = "hasAnyAuthority('students::write', 'students::read', 'users::write', 'users::read')";
    private static final String HAS_WRITE_PERMISSION = "hasAnyAuthority('students::write', 'users::write')";
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE = 1;

    private final UserService userService;
    private final StudentService studentService;
    private final RoleRepository roleRepo;
    private final CourseRepository courseRepo;
    private final GroupRepository groupRepo;

    @PreAuthorize(HAS_READ_PERMISSION)
    @GetMapping
    public String getStudentsList(Model model,
                                  @RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(DEFAULT_PAGE);
        int pageSize = size.orElse(DEFAULT_PAGE_SIZE);

        Page<StudentDto> studentsPage = studentService.findAll(
                PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.ASC, "id"))
        );
        model.addAttribute("students", studentsPage);

        int totalPages = studentsPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "students/students-list";
    }

    @PreAuthorize(HAS_WRITE_PERMISSION)
    @GetMapping("/new")
    public String getCreateStudentPage(Model model) {
        var studentForm = new CreateUserForm();
        model.addAttribute("roles", roleRepo.findAllRoleNames());
        model.addAttribute("courses", courseRepo.findAllCourseNames());
        model.addAttribute("userType", UserType.STUDENT);
        model.addAttribute("groups", groupRepo.findAllGroupNames());

        if (!model.containsAttribute("student")) {
            model.addAttribute("student", studentForm);
        }

        return "students/create-student";
    }

    @PreAuthorize(HAS_WRITE_PERMISSION)
    @PostMapping
    public String createStudent(@Valid @ModelAttribute("student") CreateUserForm student,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.student", result);
            redirectAttributes.addFlashAttribute("student", student);
            return "redirect:/students/new";
        }

        try {
            userService.create(student);
        } catch (Exception e) {
            log.error("Error saving student: [{}]", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("student", student);
            return "redirect:/students/new";
        }

        return "redirect:/students";
    }

    @PreAuthorize(HAS_WRITE_PERMISSION)
    @GetMapping("/{id}/edit")
    public String getUpdateStudentPage(Model model, @PathVariable("id") Long id) {
        var student = userService.findOne(id);
        var updateStudentForm = student.toUpdateForm();
        model.addAttribute("roles", roleRepo.findAllRoleNames());
        model.addAttribute("courses", courseRepo.findAllCourseNames());
        model.addAttribute("userType", UserType.STUDENT);
        model.addAttribute("groups", groupRepo.findAllGroupNames());

        if (!model.containsAttribute("student")) {
            model.addAttribute("student", updateStudentForm);
        }

        return "students/edit-student";
    }

    @PreAuthorize(HAS_WRITE_PERMISSION)
    @PostMapping("/{id}/edit")
    public String updateStudent(@PathVariable("id") Long id,
                                @Valid @ModelAttribute("student") UpdateUserForm student,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.student", result);
            redirectAttributes.addFlashAttribute("student", student);
            return "redirect:/students/{id}/edit";
        }

        try {
            userService.update(id, student);
        } catch (Exception e) {
            log.error("Error updating student: [{}]", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("student", student);
            return "redirect:/students/{id}/edit";
        }

        return "redirect:/students";
    }

    @PreAuthorize(HAS_WRITE_PERMISSION)
    @GetMapping("/{id}/delete")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.delete(id);
        } catch (Exception e) {
            log.error("Error deleting student: [{}]", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("toastError", e.getLocalizedMessage());
            return "redirect:/students";
        }

        redirectAttributes.addFlashAttribute("toastMessage", "Student successfully deleted!");
        return "redirect:/students";
    }
}