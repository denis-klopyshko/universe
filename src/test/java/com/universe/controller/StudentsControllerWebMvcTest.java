package com.universe.controller;

import com.universe.dto.student.StudentDto;
import com.universe.dto.user.CreateUserForm;
import com.universe.dto.user.UserResponseDto;
import com.universe.enums.UserType;
import com.universe.repository.CourseRepository;
import com.universe.repository.GroupRepository;
import com.universe.repository.RoleRepository;
import com.universe.service.StudentService;
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

@WebMvcTest(controllers = {StudentsController.class})
public class StudentsControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    StudentService studentService;

    @MockBean
    UserService userService;

    @MockBean
    RoleRepository roleRepo;

    @MockBean
    CourseRepository courseRepo;

    @MockBean
    GroupRepository groupRepo;

    @Test
    @WithMockUser
    void shouldDisplayStudentsList() throws Exception {
        when(studentService.findAll(any(PageRequest.class))).thenReturn(
                new PageImpl<>(List.of(
                        new StudentDto(1L, "John", "Doe", "john.doe@test.com", null, true, List.of(), List.of())
                ))
        );

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("students"))
                .andExpect(content().string(containsString("John Doe")))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("//tr[@data-test='student-item-row']").nodeCount(1));
    }


    @Test
    @WithMockUser(authorities = {"users::write", "users::write"})
    void shouldCreateNewStudent() throws Exception {
        ArgumentCaptor<CreateUserForm> userFormArg = ArgumentCaptor.forClass(CreateUserForm.class);
        when(userService.create(userFormArg.capture())).thenReturn(new UserResponseDto());
        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "john.doe@test.com")
                        .param("password", "pass")
                        .param("userType", "STUDENT")
                        .param("_roles", "1")
                        .param("roles", "ROLE_STUDENT")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", containsString("/students")));

        var userForm = userFormArg.getValue();
        assertThat(userForm.getEmail()).isEqualTo("john.doe@test.com");
        assertThat(userForm.getRoles().size()).isEqualTo(1);
    }

    @Test
    @WithMockUser(authorities = "users::read")
    void shouldForbidStudentCreationWithoutAuthority() throws Exception {
        mockMvc.perform(post("/students"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void shouldDisplayStudentCreateForm() throws Exception {
        when(roleRepo.findAllRoleNames()).thenReturn(
                List.of("ROLE_STUDENT", "ROLE_PROFESSOR", "ROLE_ADMIN")
        );

        mockMvc.perform(get("/students/new"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("roles", List.of("ROLE_STUDENT", "ROLE_PROFESSOR", "ROLE_ADMIN")))
                .andExpect(model().attribute("userType", UserType.STUDENT))
                .andExpect(content().string(containsString("Add Student")))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("//form[@data-test='create-student-form']").nodeCount(1));
    }

    @Test
    @WithMockUser
    void shouldDisplayStudentEditForm() throws Exception {
        when(roleRepo.findAllRoleNames()).thenReturn(
                List.of("ROLE_STUDENT", "ROLE_PROFESSOR")
        );

        when(userService.findOne(anyLong())).thenReturn(new UserResponseDto());

        mockMvc.perform(get("/students/1/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("roles", List.of("ROLE_STUDENT", "ROLE_PROFESSOR")))
                .andExpect(model().attribute("userType", UserType.STUDENT))
                .andExpect(content().string(containsString("Edit Student")))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("//form[@data-test='edit-student-form']").nodeCount(1));
    }

    @Test
    @WithMockUser(authorities = "users::write")
    void shouldDeleteStudent() throws Exception {
        doNothing().when(userService).delete(anyLong());
        mockMvc.perform(get("/students/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", containsString("/students")));
    }
}
