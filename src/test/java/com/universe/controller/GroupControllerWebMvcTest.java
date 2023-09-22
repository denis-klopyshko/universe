package com.universe.controller;

import com.universe.dto.group.CreateGroupForm;
import com.universe.dto.group.GroupResponseDto;
import com.universe.exception.ConflictException;
import com.universe.service.GroupService;
import com.universe.service.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {GroupsController.class})
public class GroupControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    GroupService groupService;

    @MockBean
    StudentService studentService;

    @Test
    @WithMockUser(authorities = "group::read")
    void shouldDisplayGroupsList() throws Exception {
        when(groupService.findAll()).thenReturn(
                List.of(
                        new GroupResponseDto(1L, "GR-101", List.of()),
                        new GroupResponseDto(1L, "GR-102", List.of())
                )
        );

        mockMvc.perform(get("/groups"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("groups"))
                .andExpect(content().string(containsString("GR-101")))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("//tr[@data-test='group-item-row']").nodeCount(2));
    }


    @Test
    @WithMockUser(authorities = "group::write")
    void shouldCreateNewGroup() throws Exception {
        ArgumentCaptor<CreateGroupForm> groupFormArg = ArgumentCaptor.forClass(CreateGroupForm.class);
        when(groupService.create(groupFormArg.capture())).thenReturn(new GroupResponseDto());

        mockMvc.perform(post("/groups")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "GR-01")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", containsString("/groups")));

        var courseForm = groupFormArg.getValue();
        assertThat(courseForm.getName()).isEqualTo("GR-01");
    }

    @Test
    @WithMockUser(authorities = "group::read")
    void shouldForbidUserCreationForTheUserWithoutAuthority() throws Exception {
        mockMvc.perform(post("/groups"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "group::write")
    void shouldDisplayGroupCreateForm() throws Exception {
        mockMvc.perform(get("/groups/new"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Add Group")))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("//form[@data-test='create-group-form']").nodeCount(1));
    }

    @Test
    @WithMockUser(authorities = "group::write")
    void shouldDisplayGroupEditForm() throws Exception {
        when(groupService.findOne(anyLong())).thenReturn(new GroupResponseDto());

        mockMvc.perform(get("/groups/1/edit"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Edit Group")))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("//form[@data-test='edit-group-form']").nodeCount(1));
    }

    @Test
    @WithMockUser(authorities = "groups::write")
    void shouldDeleteGroup() throws Exception {
        doNothing().when(groupService).delete(anyLong());
        mockMvc.perform(get("/groups/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("toastMessage", "Group successfully deleted!"))
                .andExpect(header().string("Location", containsString("/groups")));
    }

    @Test
    @WithMockUser(authorities = "groups::write")
    void shouldNotDeleteGroup_ThrowsConflictException() throws Exception {
        doThrow(new ConflictException("Error deleting group!")).when(groupService).delete(anyLong());

        mockMvc.perform(get("/groups/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", containsString("/groups")))
                .andExpect(flash().attribute("toastError", "Error deleting group!"));
    }
}
