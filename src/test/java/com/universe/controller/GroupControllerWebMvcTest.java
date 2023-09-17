package com.universe.controller;

import com.universe.dto.group.GroupDto;
import com.universe.service.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {GroupsController.class})
public class GroupControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    GroupService groupService;

    @Test
    @WithMockUser
    void shouldDisplayRoomsList() throws Exception {
        when(groupService.findAll(any(PageRequest.class))).thenReturn(
                new PageImpl<>(List.of(
                        new GroupDto(1L, "GR-101", List.of())
                ))
        );

        mockMvc.perform(get("/groups"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("content", "groups/groups-list"))
                .andExpect(model().attributeExists("groups"))
                .andExpect(content().string(containsString("GR-101")))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("//tr[@data-test='group-item-row']").nodeCount(1));
    }
}
