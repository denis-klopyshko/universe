package com.universe.controller;

import com.universe.dto.student.StudentDto;
import com.universe.service.StudentService;
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

@WebMvcTest(controllers = {StudentsController.class})
public class StudentsControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    StudentService studentService;

    @Test
    void shouldDisplayStudentsList() throws Exception {
        when(studentService.findAll(any(PageRequest.class))).thenReturn(
                new PageImpl<>(List.of(
                        new StudentDto(1L, "John", "Doe", "john.doe@test.com", null, List.of())
                ))
        );

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("content", "students"))
                .andExpect(model().attributeExists("students"))
                .andExpect(content().string(containsString("John Doe")))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("//tr[@data-test='student-item-row']").nodeCount(1));
    }
}
