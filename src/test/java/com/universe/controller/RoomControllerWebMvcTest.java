package com.universe.controller;

import com.universe.dto.RoomDto;
import com.universe.service.RoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {RoomsController.class})
public class RoomControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    RoomService roomService;

    @Test
    void shouldDisplayRoomsList() throws Exception {
        when(roomService.findAll(any(PageRequest.class))).thenReturn(
                new PageImpl<>(List.of(
                        new RoomDto(1L, "R-101")
                ))
        );

        mockMvc.perform(get("/rooms"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("content", "rooms"))
                .andExpect(model().attributeExists("rooms"))
                .andExpect(content().string(containsString("R-101")))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("//tr[@data-test='room-item-row']").nodeCount(1));
    }
}
