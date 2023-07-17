package com.universe.controller;

import com.universe.dto.course.CourseDto;
import com.universe.service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {CoursesController.class})
public class CourseControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CourseService courseService;

    @Test
    void shouldDisplayCourseList() throws Exception {
        when(courseService.findAll()).thenReturn(
                List.of(
                        CourseDto.builder().id(1L).name("Math").description("descr").build()
                )
        );

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("content", "courses"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(content().string(containsString("Math")))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("//tr[@data-test='course-item-row']").nodeCount(1));
    }
}
