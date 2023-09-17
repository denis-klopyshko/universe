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
public class UsersController {
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int DEFAULT_PAGE = 1;

    private final UserService userService;
    private final RoleRepository roleRepo;
    private final CourseRepository courseRepo;
    private final GroupRepository groupRepo;

    @PreAuthorize("hasAnyAuthority('users::write')")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String getUsersList(Model model,
                               @RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(DEFAULT_PAGE);
        int pageSize = size.orElse(DEFAULT_PAGE_SIZE);

        Page<UserResponseDto> usersPage = userService.findAll(
                PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.ASC, "id"))
        );
        model.addAttribute("users", usersPage);
        model.addAttribute("content", "users/users-list");

        int totalPages = usersPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "index";
    }

    @PreAuthorize("hasAnyAuthority('users::write')")
    @RequestMapping(value = "/users/{id}/delete", method = RequestMethod.GET)
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("message", "User successfully deleted!");
            return "redirect:/users";
        } catch (Exception e) {
            log.error("Error deleting user: " + e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("error", e.getLocalizedMessage());
            return "redirect:/users";
        }
    }

    @PreAuthorize("hasAnyAuthority('users::write')")
    @RequestMapping(value = "/users/new", method = RequestMethod.GET)
    public String getCreateUserPage(Model model) {
        var createUserRequest = new CreateUserForm();
        model.addAttribute("roles", roleRepo.findAllRoleNames());
        model.addAttribute("courses", courseRepo.findAllCourseNames());
        model.addAttribute("userTypes", UserType.getValues());
        model.addAttribute("groups", groupRepo.findAllGroupNames());
        model.addAttribute("content", "users/create-user");

        if (!model.containsAttribute("user")) {
            model.addAttribute("user", createUserRequest);
        }

        return "index";
    }

    @PreAuthorize("hasAnyAuthority('users::write')")
    @RequestMapping(value = "/users", method = RequestMethod.POST)
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
            log.error("Error saving user: " + e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/users/new";
        }

        return "redirect:/users";
    }

    @PreAuthorize("hasAnyAuthority('users::write')")
    @RequestMapping(value = "/users/{id}/edit", method = RequestMethod.GET)
    public String getUpdateUserPage(Model model, @PathVariable("id") Long id) {
        var user = userService.findOne(id);
        var updateUserRequest = user.toUpdateForm();
        model.addAttribute("roles", roleRepo.findAllRoleNames());
        model.addAttribute("courses", courseRepo.findAllCourseNames());
        model.addAttribute("userTypes", UserType.getValues());
        model.addAttribute("groups", groupRepo.findAllGroupNames());
        model.addAttribute("content", "users/edit-user");

        if (!model.containsAttribute("user")) {
            model.addAttribute("user", updateUserRequest);
        }

        return "index";
    }

    @PreAuthorize("hasAnyAuthority('users::write')")
    @RequestMapping(value = "/users/{id}/edit", method = RequestMethod.POST)
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
            log.error("Error updating user" + e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/users/{id}/edit";
        }

        return "redirect:/users";
    }
}
