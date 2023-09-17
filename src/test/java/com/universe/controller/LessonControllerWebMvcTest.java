package com.universe.controller;

import com.universe.dto.RoomDto;
import com.universe.dto.course.CourseShortDto;
import com.universe.dto.group.GroupShortDto;
import com.universe.dto.lesson.LessonDto;
import com.universe.dto.professor.ProfessorShortDto;
import com.universe.enums.LessonType;
import com.universe.service.LessonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {LessonsController.class})
public class LessonControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    LessonService lessonService;

    @Test
    @WithMockUser
    void shouldDisplayLessonsList() throws Exception {
        when(lessonService.findAll()).thenReturn(
                 List.of(
                        LessonDto.builder()
                                .id(1L)
                                .course(CourseShortDto.builder().id(1L).name("Math").build())
                                .type(LessonType.LECTURE)
                                .group(GroupShortDto.builder().id(1L).name("GR-1").build())
                                .room(RoomDto.builder().id(1L).code("R-101").build())
                                .order(1)
                                .dayOfWeek(DayOfWeek.MONDAY)
                                .weekNumber(45)
                                .professor(ProfessorShortDto.builder().build())
                                .build()
                )
        );

        mockMvc.perform(get("/lessons"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("content", "lessons/lessons-list"))
                .andExpect(model().attributeExists("lessons"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("//tr[@data-test='lesson-item-row']").nodeCount(1));
    }
}
