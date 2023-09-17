package com.universe.controller;

import com.universe.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class GroupsController {
    private final GroupService groupService;

    @GetMapping({"/groups"})
    public String getGroupsList(Model model) {
        Pageable page = PageRequest.of(0, 20, Sort.by("id").ascending());
        model.addAttribute("content", "groups/groups-list");
        model.addAttribute("groups", groupService.findAll(page).getContent());
        return "index";
    }
}
