package com.universe.controller;

import com.universe.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class RoomsController {
    private final RoomService roomService;

    @GetMapping({"/rooms"})
    public String getRoomsList(Model model) {
        Pageable page = PageRequest.of(0, 20, Sort.by("id").ascending());
        model.addAttribute("content", "rooms");
        model.addAttribute("rooms", roomService.findAll(page).getContent());
        return "index";
    }
}
