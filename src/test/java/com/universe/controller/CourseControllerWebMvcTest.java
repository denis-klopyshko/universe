package com.universe.controller;

import com.universe.dto.course.CourseResponseDto;
import com.universe.dto.course.CreateCourseForm;
import com.universe.dto.professor.ProfessorDto;
import com.universe.dto.student.StudentDto;
import com.universe.exception.ConflictException;
import com.universe.service.CourseService;
import com.universe.service.ProfessorService;
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

@WebMvcTest(controllers = {CoursesController.class})
public class CourseControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CourseService courseService;

    @MockBean
    StudentService studentService;

    @MockBean
    ProfessorService professorService;


    @Test
    @WithMockUser
    void shouldDisplayCourseList() throws Exception {
        when(courseService.findAll()).thenReturn(
                List.of(
                        CourseResponseDto.builder().id(1L).name("Math").description("descr").build()
                )
        );

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("courses"))
                .andExpect(content().string(containsString("Math")))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("//tr[@data-test='course-item-row']").nodeCount(1));
    }

    @Test
    @WithMockUser(authorities = {"course::write"})
    void shouldCreateNewCourse() throws Exception {
        ArgumentCaptor<CreateCourseForm> courseFormArg = ArgumentCaptor.forClass(CreateCourseForm.class);
        when(courseService.create(courseFormArg.capture())).thenReturn(new CourseResponseDto());

        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "Biology")
                        .param("description", "Biology Description")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", containsString("/courses")));

        var courseForm = courseFormArg.getValue();
        assertThat(courseForm.getName()).isEqualTo("Biology");
        assertThat(courseForm.getDescription()).isEqualTo("Biology Description");
    }

    @Test
    @WithMockUser(authorities = "course::read")
    void shouldForbidUserCreationForTheUserWithoutAuthority() throws Exception {
        mockMvc.perform(post("/courses"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "courses::write")
    void shouldDisplayCourseCreateForm() throws Exception {
        mockMvc.perform(get("/courses/new"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Add Course")))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("//form[@data-test='create-course-form']").nodeCount(1));
    }

    @Test
    @WithMockUser(authorities = "courses::write")
    void shouldDisplayCourseEditForm() throws Exception {
        var students = List.of(StudentDto.builder().id(1L).firstName("John").lastName("Doe").email("john.doe@test.com").build());
        var professors = List.of(ProfessorDto.builder().id(1L).firstName("Ben").lastName("Hodges").email("ben.hodges@test.com").build());

        when(studentService.findAll()).thenReturn(students);
        when(professorService.findAll()).thenReturn(professors);

        when(courseService.findOne(anyLong())).thenReturn(new CourseResponseDto());

        mockMvc.perform(get("/courses/1/edit"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("students", students))
                .andExpect(model().attribute("professors", professors))
                .andExpect(content().string(containsString("Edit Course")))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("//form[@data-test='edit-course-form']").nodeCount(1))
                .andExpect(xpath("//div[@id='students_control']").nodeCount(1))
                .andExpect(xpath("//div[@id='professors_control']").nodeCount(1));
    }

    @Test
    @WithMockUser(authorities = "courses::write")
    void shouldDeleteCourse() throws Exception {
        doNothing().when(courseService).delete(anyLong());
        mockMvc.perform(get("/courses/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("toastMessage", "Course successfully deleted!"))
                .andExpect(header().string("Location", containsString("/courses")));
    }

    @Test
    @WithMockUser(authorities = "courses::write")
    void shouldNotDeleteCourse_ThrowsConflictException() throws Exception {
        doThrow(new ConflictException("Error deleting course!")).when(courseService).delete(anyLong());

        mockMvc.perform(get("/courses/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", containsString("/courses")))
                .andExpect(flash().attribute("toastError", "Error deleting course!"));
    }
}
