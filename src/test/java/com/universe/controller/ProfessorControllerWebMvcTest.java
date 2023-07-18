package com.universe.controller;

import com.universe.dto.professor.ProfessorDto;
import com.universe.service.ProfessorService;
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

@WebMvcTest(controllers = {ProfessorsController.class})
public class ProfessorControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ProfessorService professorService;

    @Test
    void shouldDisplayProfessorsList() throws Exception {
        when(professorService.findAll(any(PageRequest.class))).thenReturn(
                new PageImpl<>( List.of(
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
                .andExpect(model().attribute("content", "professors"))
                .andExpect(model().attributeExists("professors"))
                .andExpect(content().string(containsString("John Doe")))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(xpath("//tr[@data-test='professor-item-row']").nodeCount(1));
    }
}
