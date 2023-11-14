package com.universe.controller;

import com.universe.dto.user.CreateUserForm;
import com.universe.dto.user.UpdateUserForm;
import com.universe.dto.user.UserResponseDto;
import com.universe.enums.UserType;
import com.universe.repository.CourseRepository;
import com.universe.repository.GroupRepository;
import com.universe.repository.RoleRepository;
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
@RequestMapping("/users")
public class UsersController {
    private static final String HAS_READ_PERMISSION = "hasAnyAuthority('users::write', 'users::read')";
    private static final String HAS_WRITE_PERMISSION = "hasAnyAuthority('users::write')";
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_PAGE = 1;

    private final UserService userService;
    private final RoleRepository roleRepo;
    private final CourseRepository courseRepo;
    private final GroupRepository groupRepo;

    @PreAuthorize(HAS_READ_PERMISSION)
    @GetMapping
    public String getUsersList(Model model,
                               @RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(DEFAULT_PAGE);
        int pageSize = size.orElse(DEFAULT_PAGE_SIZE);

        Page<UserResponseDto> usersPage = userService.findAll(
                PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.ASC, "id"))
        );
        model.addAttribute("users", usersPage);

        int totalPages = usersPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "users/users-list";
    }

    @PreAuthorize(HAS_WRITE_PERMISSION)
    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("toastMessage", "User successfully deleted!");
            return "redirect:/users";
        } catch (Exception e) {
            log.error("Error deleting user: [{}]", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("toastError", e.getLocalizedMessage());
            return "redirect:/users";
        }
    }

    @PreAuthorize(HAS_WRITE_PERMISSION)
    @GetMapping("/new")
    public String getCreateUserPage(Model model) {
        var createUserRequest = new CreateUserForm();
        model.addAttribute("roles", roleRepo.findAllRoleNames());
        model.addAttribute("courses", courseRepo.findAllCourseNames());
        model.addAttribute("userTypes", UserType.getValues());
        model.addAttribute("groups", groupRepo.findAllGroupNames());

        if (!model.containsAttribute("user")) {
            model.addAttribute("user", createUserRequest);
        }

        return "users/create-user";
    }

    @PreAuthorize(HAS_WRITE_PERMISSION)
    @PostMapping
    public String createUser(@Valid @ModelAttribute("user") CreateUserForm user,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/users/new";
        }

        try {
            userService.create(user);
        } catch (Exception e) {
            log.error("Error saving user: [{}]", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/users/new";
        }

        return "redirect:/users";
    }

    @PreAuthorize(HAS_WRITE_PERMISSION)
    @GetMapping("/{id}/edit")
    public String getUpdateUserPage(Model model, @PathVariable("id") Long id) {
        var user = userService.findOne(id);
        var updateUserRequest = user.toUpdateForm();
        model.addAttribute("roles", roleRepo.findAllRoleNames());
        model.addAttribute("courses", courseRepo.findAllCourseNames());
        model.addAttribute("userTypes", UserType.getValues());
        model.addAttribute("groups", groupRepo.findAllGroupNames());

        if (!model.containsAttribute("user")) {
            model.addAttribute("user", updateUserRequest);
        }

        return "users/edit-user";
    }

    @PreAuthorize(HAS_WRITE_PERMISSION)
    @PostMapping("/{id}/edit")
    public String updateUser(@PathVariable("id") Long id,
                             @Valid @ModelAttribute("user") UpdateUserForm user,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", result);
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/users/{id}/edit";
        }

        try {
            userService.update(id, user);
        } catch (Exception e) {
            log.error("Error updating user: [{}]", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/users/{id}/edit";
        }

        return "redirect:/users";
    }
}
