package com.universe.controller;

import com.universe.dto.group.CreateGroupForm;
import com.universe.dto.group.EditGroupForm;
import com.universe.mapping.GroupMapper;
import com.universe.service.GroupService;
import com.universe.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/groups")
public class GroupsController {
    private static final String HAS_READ_PERMISSION = "hasAnyAuthority({'groups::write', 'groups::read'})";
    private static final String HAS_WRITE_PERMISSION = "hasAnyAuthority('groups::write')";

    private final GroupService groupService;
    private final StudentService studentService;

    @PreAuthorize(HAS_READ_PERMISSION)
    @RequestMapping(method = RequestMethod.GET)
    public String getGroupsListPage(Model model) {
        model.addAttribute("groups", groupService.findAll());
        return "groups/groups-list";
    }


    @PreAuthorize(HAS_WRITE_PERMISSION)
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String getCreateGroupPage(Model model) {
        if (!model.containsAttribute("group")) {
            model.addAttribute("group", new CreateGroupForm());
        }

        return "groups/create-group";
    }

    @PreAuthorize(HAS_WRITE_PERMISSION)
    @RequestMapping(method = RequestMethod.POST)
    public String createGroup(@Valid @ModelAttribute("course") CreateGroupForm group,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.group", result);
            redirectAttributes.addFlashAttribute("group", group);
            return "redirect:/groups/new";
        }

        try {
            groupService.create(group);
        } catch (Exception e) {
            log.error("Error creating group: [{}]", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("group", group);
            return "redirect:/groups/new";
        }

        redirectAttributes.addFlashAttribute("toastMessage",
                String.format("Group [ %s ] successfully created!", group.getName()));
        return "redirect:/groups";
    }

    @PreAuthorize(HAS_WRITE_PERMISSION)
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String getEditGroupPage(Model model, @PathVariable("id") Long id) {
        var group = groupService.findOne(id);
        var editGroupForm = GroupMapper.INSTANCE.mapToEditForm(group);

        model.addAttribute("students", studentService.findAll());

        if (!model.containsAttribute("group")) {
            model.addAttribute("group", editGroupForm);
        }

        return "groups/edit-group";
    }

    @PreAuthorize(HAS_WRITE_PERMISSION)
    @RequestMapping(value = "/{id}/edit", method = RequestMethod.POST)
    public String updateGroup(@PathVariable("id") Long id,
                              @Valid @ModelAttribute("group") EditGroupForm editForm,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.group", result);
            redirectAttributes.addFlashAttribute("group", editForm);
            return "redirect:/groups/{id}/edit";
        }

        try {
            groupService.update(id, editForm);
        } catch (Exception e) {
            log.error("Error updating group: [{}]", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("errorMessage", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("group", editForm);
            return "redirect:/groups/{id}/edit";
        }

        redirectAttributes.addFlashAttribute("toastMessage", "Group successfully updated!");
        return "redirect:/groups";
    }

    @PreAuthorize(HAS_WRITE_PERMISSION)
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    public String deleteGroup(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            groupService.delete(id);
        } catch (Exception e) {
            log.error("Error deleting group: [{}]", e.getLocalizedMessage());
            redirectAttributes.addFlashAttribute("toastError", e.getLocalizedMessage());
            return "redirect:/groups";
        }

        redirectAttributes.addFlashAttribute("toastMessage", "Group successfully deleted!");
        return "redirect:/groups";
    }
}
