package com.universe.controller;

import com.universe.dto.user.CreateUserForm;
import com.universe.dto.user.UserResponseDto;
import com.universe.enums.UserType;
import com.universe.repository.CourseRepository;
import com.universe.repository.GroupRepository;
import com.universe.repository.RoleRepository;
import com.universe.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {UsersController.class})
public class UsersControllerWebMvcTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    RoleRepository roleRepo;

    @MockBean
    CourseRepository courseRepo;

    @MockBean
    GroupRepository groupRepo;

    @Test
    @WithMockUser(authorities = "users::read")
    void shouldNotDisplayAddUserButtonWithoutPermission() throws Exception {
        when(userService.findAll(any(PageRequest.class))).thenReturn(
                new PageImpl<>(List.of(
                        UserResponseDto.builder()
                                .id(1L)
                                .firstName("John")
                                .lastName("Doe")
                                .email("john.doe@test.com")
                                .build()
                ))
        );

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("content", "users/users-list"))
                .andExpect(model().attributeExists("users"))
                .andExpect(content().string(containsString("John Doe")))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("//div[@data-test='add-user-btn']").nodeCount(0));
    }

    @Test
    @WithMockUser(authorities = {"users::write"})
    void shouldCreateNewUser() throws Exception {
        ArgumentCaptor<CreateUserForm> userFormArg = ArgumentCaptor.forClass(CreateUserForm.class);
        when(userService.create(userFormArg.capture())).thenReturn(new UserResponseDto());
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "john.doe@test.com")
                        .param("password", "pass")
                        .param("userType", "PROFESSOR")
                        .param("_roles", "1")
                        .param("roles", "ROLE_PROFESSOR")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", containsString("/users")));

        var userForm = userFormArg.getValue();
        assertThat(userForm.getEmail()).isEqualTo("john.doe@test.com");
        assertThat(userForm.getRoles().size()).isEqualTo(1);
    }

    @Test
    @WithMockUser(authorities = "users::read")
    void shouldForbidUserCreationForTheUserWithoutAuthority() throws Exception {
        mockMvc.perform(post("/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void shouldDisplayUserCreateForm() throws Exception {
        when(roleRepo.findAllRoleNames()).thenReturn(
                List.of("ROLE_STUDENT", "ROLE_PROFESSOR", "ROLE_ADMIN")
        );

        mockMvc.perform(get("/users/new"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("content", "users/create-user"))
                .andExpect(model().attribute("roles", List.of("ROLE_STUDENT", "ROLE_PROFESSOR", "ROLE_ADMIN")))
                .andExpect(model().attribute("userTypes", UserType.getValues()))
                .andExpect(content().string(containsString("Add User")))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("//form[@data-test='create-user-form']").nodeCount(1));
    }

    @Test
    @WithMockUser
    void shouldDisplayUserEditForm() throws Exception {
        when(roleRepo.findAllRoleNames()).thenReturn(
                List.of("ROLE_STUDENT", "ROLE_PROFESSOR")
        );

        when(userService.findOne(anyLong())).thenReturn(new UserResponseDto());

        mockMvc.perform(get("/users/1/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("content", "users/edit-user"))
                .andExpect(model().attribute("roles", List.of("ROLE_STUDENT", "ROLE_PROFESSOR")))
                .andExpect(model().attribute("userTypes", UserType.getValues()))
                .andExpect(content().string(containsString("Edit User")))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("//form[@data-test='edit-user-form']").nodeCount(1));
    }

    @Test
    @WithMockUser(authorities = "users::write")
    void shouldDeleteUser() throws Exception {
        doNothing().when(userService).delete(anyLong());
        mockMvc.perform(get("/users/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", containsString("/users")));
    }
}
