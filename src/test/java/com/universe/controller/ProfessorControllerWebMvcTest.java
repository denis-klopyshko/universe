package com.universe.controller;

import com.universe.dto.professor.ProfessorDto;
import com.universe.dto.user.CreateUserForm;
import com.universe.dto.user.UserResponseDto;
import com.universe.enums.UserType;
import com.universe.repository.CourseRepository;
import com.universe.repository.RoleRepository;
import com.universe.service.ProfessorService;
import com.universe.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {ProfessorsController.class})
public class ProfessorControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ProfessorService professorService;

    @MockBean
    UserService userService;

    @MockBean
    RoleRepository roleRepo;

    @MockBean
    CourseRepository courseRepo;

    @Test
    @WithMockUser
    void shouldDisplayProfessorsList() throws Exception {
        when(professorService.findAll(any(PageRequest.class))).thenReturn(
                new PageImpl<>(List.of(
                        ProfessorDto.builder()
                                .id(1L)
                                .firstName("John")
                                .lastName("Doe")
                                .email("john.doe@test.com")
                                .build()
                ))
        );

        mockMvc.perform(get("/professors"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("professors"))
                .andExpect(content().string(containsString("John Doe")))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("//tr[@data-test='professor-item-row']").nodeCount(1));
    }

    @Test
    @WithMockUser(authorities = "users::read")
    void shouldNotDisplayAddProfessorButtonWithoutPermission() throws Exception {
        when(professorService.findAll(any(PageRequest.class))).thenReturn(
                new PageImpl<>(List.of(
                        ProfessorDto.builder()
                                .id(1L)
                                .firstName("John")
                                .lastName("Doe")
                                .email("john.doe@test.com")
                                .build()
                ))
        );

        mockMvc.perform(get("/professors"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("professors"))
                .andExpect(content().string(containsString("john.doe@test.com")))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("//div[@data-test='add-professor-btn']").nodeCount(0));
    }

    @Test
    @WithMockUser(authorities = {"professors::write", "users::write"})
    void shouldCreateNewProfessor() throws Exception {
        when(userService.create(any(CreateUserForm.class))).thenReturn(new UserResponseDto());
        mockMvc.perform(post("/professors")
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
                .andExpect(header().string("Location", containsString("/professors")));
    }

    @Test
    @WithMockUser(authorities = "users::read")
    void shouldForbidUserCreationForTheUserWithoutAuthority() throws Exception {
        mockMvc.perform(post("/professors"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void shouldDisplayProfessorCreateForm() throws Exception {
        when(roleRepo.findAllRoleNames()).thenReturn(
                List.of("ROLE_STUDENT", "ROLE_PROFESSOR")
        );

        mockMvc.perform(get("/professors/new"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("roles", List.of("ROLE_STUDENT", "ROLE_PROFESSOR")))
                .andExpect(model().attribute("userType", UserType.PROFESSOR))
                .andExpect(content().string(containsString("Add Professor")))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("//form[@data-test='create-professor-form']").nodeCount(1));
    }

    @Test
    @WithMockUser
    void shouldDisplayProfessorEditForm() throws Exception {
        when(roleRepo.findAllRoleNames()).thenReturn(
                List.of("ROLE_STUDENT", "ROLE_PROFESSOR")
        );

        when(userService.findOne(anyLong())).thenReturn(new UserResponseDto());

        mockMvc.perform(get("/professors/1/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("roles", List.of("ROLE_STUDENT", "ROLE_PROFESSOR")))
                .andExpect(model().attribute("userType", UserType.PROFESSOR))
                .andExpect(content().string(containsString("Edit Professor")))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("//form[@data-test='edit-professor-form']").nodeCount(1));
    }

    @Test
    @WithMockUser(authorities = "users::write")
    void shouldDeleteProfessor() throws Exception {
        doNothing().when(userService).delete(anyLong());
        mockMvc.perform(get("/professors/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", containsString("/professors")));
    }
}
